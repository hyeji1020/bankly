package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.AccountNotFoundException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.exception.UserNotFoundException;
import com.project.bankassetor.model.entity.Account;
import com.project.bankassetor.model.entity.User;
import com.project.bankassetor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("아이디 {}: 에 해당하는 사용자를 찾을 수 없습니다.", id);
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        });
    }
}
