package voicerecipeserver.recommend;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Mark;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.respository.MarkRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.domain.JwtAuthentication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.StreamSupport;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;

/**
 * Slope One algorithm implementation
 */
@Service
public class SlopeOne {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;
    private  Map<Recipe, Map<Recipe, Double>> diff = new HashMap<>();
    private  Map<Recipe, Map<Recipe, Integer>> freq = new HashMap<>();
    private  Map<User, HashMap<Recipe, Double>> inputData;
    private  Map<User, HashMap<Recipe, Double>> outputData = new HashMap<>();

    @Autowired
    public SlopeOne(ModelMapper mapper, UserRepository userRepository, MarkRepository markRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
    }

    public List<RecipeDto> slopeOne(Integer limit) throws AuthException {
        if (limit == null) {
            limit = 100;
        }
        inputData = initializeData();
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
        System.out.println("\nSlope One - With Predictions\n");

        return predict(inputData, limit);
    }

    private  Map<User, HashMap<Recipe, Double>> initializeData() {
        List<Mark> markList = StreamSupport.stream(markRepository.findAll().spliterator(), false).toList();
        HashMap<Recipe, Double> userRecipesMarked;
        Map<User, HashMap<Recipe, Double>> data = new HashMap<>();
        for (Mark m : markList) {
            System.out.println(m);
            if (data.containsKey(m.getUser())) {
                userRecipesMarked = data.get(m.getUser());
            } else {
                userRecipesMarked = new HashMap<Recipe, Double>();
            }
            userRecipesMarked.put(m.getRecipe(),(double)  m.getMark());
            data.put(m.getUser(), userRecipesMarked);
        }
//
//        HashMap<Recipe, Double> newUser;
//        Set<Recipe> newRecommendationSet;
//        for (int i = 0; i < numberOfUsers; i++) {
//            newUser = new HashMap<Recipe, Double>();
//            newRecommendationSet = new HashSet<>();
//            for (int j = 0; j < 3; j++) {
//                newRecommendationSet.add(items.get((int) (Math.random() * 5)));
//            }
//            for (Recipe Recipe : newRecommendationSet) {
//                newUser.put(Recipe, Math.random());
//            }
//            data.put(new User("User " + i), newUser);
//        }
        return data;
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data existing user data and their items' ratings
     */
    private  void buildDifferencesMatrix(Map<User, HashMap<Recipe, Double>> data) {
        for (HashMap<Recipe, Double> user : data.values()) {
            for (Entry<Recipe, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Recipe, Double>());
                    freq.put(e.getKey(), new HashMap<Recipe, Integer>());
                }
                for (Entry<Recipe, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Recipe j : diff.keySet()) {
            for (Recipe i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data);
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data  existing user data and their items' ratings
     * @param limit
     */
    private  List<RecipeDto> predict(Map<User, HashMap<Recipe, Double>> data, Integer limit) throws AuthException {
        HashMap<Recipe, Double> uPred = new HashMap<Recipe, Double>();
        HashMap<Recipe, Integer> uFreq = new HashMap<Recipe, Integer>();
        for (Recipe j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Entry<User, HashMap<Recipe, Double>> e : data.entrySet()) {
            for (Recipe j : e.getValue().keySet()) {
                for (Recipe k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Recipe, Double> clean = new HashMap<Recipe, Double>();
            for (Recipe j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
//            InputData inputDataa = new InputData(); //todo получаем рецепты
            List<Mark> markList = StreamSupport.stream(markRepository.findAll().spliterator(), false).toList();

            for (Mark j : markList) {
                if (e.getValue().containsKey(j.getRecipe())) {
                    clean.put(j.getRecipe(), e.getValue().get(j.getRecipe()));
                } else if (!clean.containsKey(j.getRecipe())) {
                    clean.put(j.getRecipe(), -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
        printData(outputData);

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            JwtAuthentication principal = getAuthInfo();
            if (principal == null) {
                return null;
            }
            User user = userRepository.findByUid(principal.getLogin()).orElseThrow(() -> new AuthException("Такой пользователь не зарегистрирован"));

            List<Recipe> sortedList = outputData.get(user).entrySet().stream()
                    .sorted(Map.Entry.<Recipe, Double>comparingByValue().reversed())
                    .limit(limit)
                    .map(Map.Entry::getKey)
                    .toList();
            List<RecipeDto> recipeDtos =  mapper.map(sortedList, new TypeToken<List<RecipeDto>>() {
            }.getType());
            return recipeDtos;
        }
        return null;
    }

    private  void printData(Map<User, HashMap<Recipe, Double>> data) {
        for (User user : data.keySet()) {
            System.out.println(user.getUid() + ":");
            print(data.get(user));
        }
    }

    private  void print(HashMap<Recipe, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Recipe j : hashMap.keySet()) {
            System.out.println(" " + j.getName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

}

