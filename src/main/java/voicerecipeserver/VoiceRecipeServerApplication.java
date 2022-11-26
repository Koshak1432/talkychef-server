package voicerecipeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO Каскадное удаление нужно сделать для рецепта (картинки и шаги должны дропнуться). Вроде на уровне hibernate это я прописал, но вот в БД такого нет.
// TODO в бд холостое увеличение ID в сиквенсах происходит при неудачной попытке вставить значения
// TODO тригеров сделать для дропа ненужных картинок, сетов единиц и т.д.
// TODO проверить генерацию последовательностей для каждой таблицы
// TODO категории реализовать
@SpringBootApplication
public class VoiceRecipeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceRecipeServerApplication.class, args);
    }

}
