package kr.co.anabada.admin.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.anabada.admin.entity.Admin;
import kr.co.anabada.admin.repository.AdminRepository;
import kr.co.anabada.user.entity.User;
import kr.co.anabada.user.repository.UserRepository;

@Service
public class AdminService {
   @Autowired
   private UserRepository userRepo;
   
   @Autowired
   private AdminRepository adminRepo;
   
   private User getUserById(Integer userNo) {
        return userRepo.findById(userNo).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
   
   // 관리자 찾기
   public Admin findByUserNo(Integer userNo) {
      User user = getUserById(userNo);
      return adminRepo.findByUserNo(user);
   }
}

