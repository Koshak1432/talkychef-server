package talkychefserver.utils;

import talkychefserver.config.Constants;

public class GetUtil {
    public static int getCurrentLimit(Integer possibleLimit) {
        return possibleLimit == null ? Constants.MAX_ITEMS_PER_PAGE : possibleLimit;
    }

    public static int getCurrentPage(Integer possiblePage) {
        return possiblePage == null ? 0 : possiblePage;
    }
}
