package com.cvconnect.utils;

import com.cvconnect.dto.common.Identifiable;
import nmquan.commonlib.dto.BaseDto;
import nmquan.commonlib.dto.response.FilterResponse;
import nmquan.commonlib.exception.AppException;
import nmquan.commonlib.exception.CommonErrorCode;
import nmquan.commonlib.utils.WebUtils;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoreServiceUtils {

    public static Long getCurrentOrgId(){
        Long orgId = WebUtils.getCurrentOrgId();
        if(Objects.isNull(orgId)){
            throw new AppException(CommonErrorCode.ACCESS_DENIED);
        }
        return orgId;
    }

    public static <T extends Identifiable,G extends Identifiable> List<Long> getDeleteIds(List<T> newObjects, List<G> oldObjects){
        List<Long> idsInNew = newObjects.stream()
                .map(T::getId)
                .filter(Objects::nonNull)
                .toList();
        return oldObjects.stream()
                .map(G::getId)
                .filter(id -> !idsInNew.contains(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T extends BaseDto<Instant>> List<T> configResponsePublic(List<T> dtos){
        if(Objects.nonNull(dtos)){
            for(T dto : dtos){
                dto.setIsActive(null);
                dto.setIsDeleted(null);
                dto.setCreatedBy(null);
                dto.setUpdatedBy(null);
                dto.setCreatedAt(null);
                dto.setUpdatedAt(null);
            }
        }
        return dtos;
    }

    public static <T extends BaseDto<Instant>> FilterResponse<T> configResponsePublic(FilterResponse<T> response){
        if(Objects.nonNull(response) && Objects.nonNull(response.getData())){
            for(T dto : response.getData()){
                dto.setIsActive(null);
                dto.setIsDeleted(null);
                dto.setCreatedBy(null);
                dto.setUpdatedBy(null);
                dto.setCreatedAt(null);
                dto.setUpdatedAt(null);
            }
        }
        return response;
    }
}
