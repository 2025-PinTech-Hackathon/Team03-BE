package com.example.fintech.domain.spendingConstraint.service;

import com.example.fintech.domain.quest.exception.QuestErrorCode;
import com.example.fintech.domain.quest.exception.QuestException;
import com.example.fintech.domain.spendingConstraint.converter.SpendingConstraintConverter;
import com.example.fintech.domain.spendingConstraint.dto.request.SpendingConstraintsRequestDTO;
import com.example.fintech.domain.spendingConstraint.entity.SpendingConstraint;
import com.example.fintech.domain.spendingConstraint.repository.SpendingConstraintRepository;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.user.repository.UserRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingConstraintServiceImpl implements SpendingConstraintService{

    private final CustomJwtUtil CustomJwtUtil;
    private final UserRepository userRepository;
    private final SpendingConstraintRepository constraintRepository;
    private final SpendingConstraintConverter spendingConstraintConverter;

    @Override
    public void putSpendingLimits(String authHeader, SpendingConstraintsRequestDTO request) {

        Long userId = CustomJwtUtil.getUserId(authHeader);

        User parent = userRepository.findById(userId)
                .orElseThrow(() -> new QuestException(QuestErrorCode.USER_NOT_FOUND));

        List<User> children = parent.getChildren();
        if (children.isEmpty()) {
            throw new QuestException(QuestErrorCode.CHILD_NOT_FOUND);
        }

        User child = children.get(0);
        Long childId = child.getId();
        Optional<SpendingConstraint> constraint = constraintRepository.findByUserId(childId);

        if(constraint.isPresent()){
            SpendingConstraint existingConstraint = constraint.get();
            existingConstraint.updateFrom(request);
            constraintRepository.save(existingConstraint);
        }
        else{
            SpendingConstraint newConstraint = spendingConstraintConverter.toEntity(request, child);
            constraintRepository.save(newConstraint);

        }



    }
}
