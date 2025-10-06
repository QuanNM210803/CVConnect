package com.cvconnect.enums;

import com.cvconnect.dto.enums.EliminateReasonEnumDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum EliminateReasonEnum {
    LACK_OF_EXPERIENCE("Thiếu kinh nghiệm"),
    SKILL_MISMATCH("Kỹ năng không phù hợp"),
    OVERQUALIFIED("Trình độ vượt quá yêu cầu vị trí"),
    CULTURE_MISMATCH("Không phù hợp văn hóa doanh nghiệp"),
    SALARY_EXPECTATION_TOO_HIGH("Mức lương mong muốn vượt ngân sách"),
    FAILED_ABILITY_TEST("Không đạt bài kiểm tra năng lực"),
    FAILED_INTERVIEW("Không đạt trong buổi phỏng vấn"),
    POSITION_FILLED("Vị trí đã được tuyển"),
    LACK_OF_COMMUNICATION_SKILLS("Kỹ năng giao tiếp chưa tốt"),
    UNRESPONSIVE("Ứng viên không phản hồi sau khi liên hệ"),
    DID_NOT_ATTEND_INTERVIEW("Không tham dự buổi phỏng vấn"),
    DID_NOT_ONBOARD("Không onboard sau khi được chấp nhận"),
    BACKGROUND_CHECK_FAILED("Lý lịch không đạt"),
    UNSUITABLE_ATTITUDE("Thái độ không phù hợp trong quá trình phỏng vấn"),
    OTHERS("Lý do khác");

    private final String description;

    EliminateReasonEnum(String description) {
        this.description = description;
    }

    public static EliminateReasonEnum getEliminateReasonEnum(String name) {
        for (EliminateReasonEnum eliminateReason : EliminateReasonEnum.values()) {
            if (eliminateReason.name().equalsIgnoreCase(name)) {
                return eliminateReason;
            }
        }
        return null;
    }

    public static EliminateReasonEnumDto getEliminateReasonEnumDto(EliminateReasonEnum eliminateReason) {
        if (Objects.isNull(eliminateReason)) {
            return null;
        }
        return EliminateReasonEnumDto.builder()
                .description(eliminateReason.getDescription())
                .build();
    }

    public static EliminateReasonEnumDto getEliminateReasonEnumDto(String name) {
        EliminateReasonEnum eliminateReason = getEliminateReasonEnum(name);
        return getEliminateReasonEnumDto(eliminateReason);
    }

    public static List<EliminateReasonEnumDto> getAll() {
        return Arrays.stream(EliminateReasonEnum.values())
                .map(EliminateReasonEnum::getEliminateReasonEnumDto)
                .toList();
    }
}
