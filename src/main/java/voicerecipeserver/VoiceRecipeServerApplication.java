package voicerecipeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO Каскадное удаление нужно сделать для картинок. Ибо в БД сейчас картинки не дропаются, если шаг был удален. Мб через сервак это сделать.
// TODO в бд холостое увеличение ID в сиквенсах происходит при неудачной попытке вставить значения
// TODO тригеров сделать для дропа ненужных картинок, сетов единиц и т.д.
// TODO проверить генерацию последовательностей для каждой таблицы
// TODO категории реализовать
// TODO cross-origin настроить только на сайт. Сейчас всё разрешил.
// TODO в названиях ингредиентов/единиц измерений запретить спецсимволы и цифры
// TODO на каждый сценарий добавления тестик бы бахнуть
// TODO manytomany нужно на set-ах делать. Читай подробнее.
// TODO эксепшены вообще не обрабатываются нормально, обычно тупо 500 отсылает
// TODO логирование сделать
@SpringBootApplication
public class VoiceRecipeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceRecipeServerApplication.class, args);
    }

}
