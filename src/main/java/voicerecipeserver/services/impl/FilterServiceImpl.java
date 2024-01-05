package voicerecipeserver.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.entities.Time;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.*;
//import voicerecipeserver.respository.FilterRepository;
import voicerecipeserver.services.FilterService;
import voicerecipeserver.utils.FindUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    //    private final FilterRepository filterRepository;
    private CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final TimeRepository timeRepository;


    public FilterServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository, TimeRepository timeRepository) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public ResponseEntity<FilterDto> getFilters() throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        if (cookie == null) {
            if (requestAttributes != null) {
                HttpServletResponse response = requestAttributes.getResponse();
                if (response != null) {
                    addFilterCookie(new FilterDto(), response);
                }
            }
        }
        httpServletRequest = requestAttributes.getRequest();
        cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        return ResponseEntity.ok(filterDto);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipes(FilterDto body) {
        return null;
    }

    @Override
    public ResponseEntity<FilterDto> postCategoryHelper(Long id) throws NotFoundException, IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = changeCategory(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }


    @Override
    public ResponseEntity<FilterDto> helperPostTime(Long id) throws IOException, NotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = changeTime(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }

    @Override
    public ResponseEntity<FilterDto> helperPostIngredient(Long id) throws IOException, NotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = changeIngredient(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }

    @Override
    public ResponseEntity<FilterDto> helperDeleteCategory(Long id) throws IOException, NotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = deleteCategory(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }


    @Override
    public ResponseEntity<FilterDto> helperDeleteTime(Long id) throws IOException, NotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = deleteTime(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }

    @Override
    public ResponseEntity<FilterDto> helperDeleteIngredient(Long id) throws IOException, NotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Cookie cookie = getFilterFromCookies(httpServletRequest);
        FilterDto filterDto = extractFilter(cookie, requestAttributes);
        if (requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if (response != null) {
                filterDto = deleteIngredient(filterDto, id);
                saveCookie(filterDto, response, cookie);
            }
        }
        return ResponseEntity.ok(filterDto);
    }

    @Override
    public ResponseEntity<List<TimeDto>> getTimes() {
        List<Time> times = timeRepository.findAll();
        List<TimeDto> timeDtos = times.stream().map(
                element -> mapper.map(element, TimeDto.class)).toList();
        return ResponseEntity.ok(timeDtos);
    }

    public Cookie getFilterFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("filters".equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void addFilterCookie(FilterDto filterDto, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("filters", convertToString(filterDto));
        System.out.println(convertToString(filterDto));
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String convertToString(FilterDto filterDto) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(filterDto);
        return URLEncoder.encode(json, StandardCharsets.UTF_8.toString());
    }

    private void saveCookie(FilterDto filterDto, HttpServletResponse response, Cookie cookie) throws UnsupportedEncodingException, JsonProcessingException {
        cookie.setValue(convertToString(filterDto));
        cookie.setPath("/");
        response.setContentType("application/json");
        response.addCookie(cookie);
    }

    private FilterDto changeCategory(FilterDto filterDto, Long id) throws NotFoundException {
        Category category = FindUtils.findCategory(categoryRepository, id);
        if (filterDto.getCategory().isEmpty()) {
            filterDto.setCategory(Collections.singleton(mapper.map(category, CategoryDto.class)));
        } else {
            filterDto.getCategory().add(mapper.map(category, CategoryDto.class));
        }
        return filterDto;
    }

    private FilterDto deleteCategory(FilterDto filterDto, Long id) throws NotFoundException {
        Category category = FindUtils.findCategory(categoryRepository, id);
        filterDto.getCategory().remove(mapper.map(category, CategoryDto.class));
        return filterDto;
    }

    private FilterDto changeIngredient(FilterDto filterDto, Long id) throws NotFoundException {
        Ingredient ingredient = FindUtils.findIngredient(ingredientRepository, id);
        if (filterDto.getIngredients().isEmpty()) {
            filterDto.setIngredients(Collections.singleton(mapper.map(ingredient, IngredientDto.class)));
        } else {
            filterDto.getIngredients().add(mapper.map(ingredient, IngredientDto.class));
        }
        return filterDto;
    }

    private FilterDto deleteIngredient(FilterDto filterDto, Long id) throws NotFoundException {
        Ingredient ingredient = FindUtils.findIngredient(ingredientRepository, id);
        filterDto.getIngredients().remove(mapper.map(ingredient, IngredientDto.class));
        return filterDto;
    }

    private FilterDto changeTime(FilterDto filterDto, Long id) throws NotFoundException {
        Time time = FindUtils.findTime(timeRepository, id);
        if (filterDto.getTime().isEmpty()) {
            filterDto.setTime(Collections.singleton(mapper.map(time, TimeDto.class)));
        } else {
            filterDto.getTime().add(mapper.map(time, TimeDto.class));
        }
        return filterDto;
    }

    private FilterDto deleteTime(FilterDto filterDto, Long id) throws NotFoundException {
        Time time = FindUtils.findTime(timeRepository, id);
        filterDto.getTime().remove(mapper.map(time, TimeDto.class));
        return filterDto;
    }

    private FilterDto extractFilter(Cookie cookie, ServletRequestAttributes requestAttributes) throws IOException {
        if (cookie == null) addFilterCookie(new FilterDto(), requestAttributes.getResponse());
        String filterData = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.toString());
        ObjectMapper objMapper = new ObjectMapper();
        FilterDto filterDto = objMapper.readValue(filterData, FilterDto.class);
        if (filterDto == null) {
            filterDto = new FilterDto();
        }
        return filterDto;
    }

}
