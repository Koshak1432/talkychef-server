package talkychefserver.model.dto;

import lombok.Getter;

@Getter
public enum VoiceCommand {
    NOTHING(-1),
    TO_MENU(0),
    NEXT(1),
    BACK(2),
    DETAILS(3),
    OPEN_SELECTIONS(4),
    SAY(5),
    START_TIMER(6),
    STOP_TIMER(7),
    CLOSE(8),
    STOP_SAY(9),
    RESET_TIMER(10);

    private final int value;

    VoiceCommand(int value) {
        this.value = value;
    }

    public static VoiceCommand getByValue(int value) {
        for (VoiceCommand command : VoiceCommand.values()) {
            if (command.value == value) {
                return command;
            }
        }
        throw new IllegalArgumentException("No command with value: " + value);
    }

}
