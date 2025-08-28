package com.yourcompany.user.service.service;


import brave.http.HttpServerRequest;
import com.yourcompany.user.service.dto.InvoiceDTO;
import com.yourcompany.user.service.dto.UserInfo;
import com.yourcompany.user.service.entities.UserE;
import com.yourcompany.user.service.exception.ApiException;
import com.yourcompany.user.service.exception.UserException;
import com.yourcompany.user.service.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final InvoiceService invoiceService;
    private final UserRepository userRepository;

    public List<UserInfo> getUsers(List<String> ids, HttpServletRequest request) {


        if(CollectionUtils.isEmpty(ids)) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Bad request", request.getPathInfo(),List.of("userIds are missing in request."));
        }

        log.info("Calling database with ids : {} ", ids);
        List<UserE> userEList = userRepository.findAllById(ids);

        log.info("Sending response back.");

        return userEList.stream()
                .map((user)->this.getUserInfo(user, request))
                .toList();
    }

    private UserInfo getUserInfo(UserE user, HttpServletRequest request) {

        log.info("calling invoice-service for user-id : {} ", user.getId());
        List<InvoiceDTO> invoiceDTOS = invoiceService.callInvoiceServiceAndGetInvoiceDTOList(user.getId(), request);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setAge(user.getAge());
        userInfo.setGender(user.getGender());
        userInfo.setCreatedTime(user.getCreatedTime());
        userInfo.setUpdatedTime(user.getUpdatedTime());
        userInfo.setInvoiceDTOList(invoiceDTOS);

        return userInfo;
    }

}
