package com.ksh.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.dto.email.MailDto;
import com.ksh.jwt.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
	@Autowired
    private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "quizappapiserver@gmail.com";

    @Transactional
    public MailDto createMailAndChangePassword(String userEmail, String userName){
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(userEmail);
        dto.setTitle(userName+"님의 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시비밀번호 안내 관련 이메일 입니다." + "[" + userName + "]" +"님의 임시 비밀번호는 "
        + str + " 입니다.");
        updatePassword(str,userEmail);
        return dto;
    }
    
    public MailDto checkEmail(String userEmail) {
    	System.out.println("checkEmail"+ userEmail);
    	String str =getTempPassword();
    	MailDto dto = new MailDto();
    	dto.setAddress(userEmail);
    	dto.setTitle("퀴즈 앱 가입 인증 안내 이메일입니다.");
    	dto.setMessage("안녕하세요. 가입 인증 코드 안내 관련 이메일 입니다.\n 인증 번호는 : "+str+" 입니다.");
    	dto.setAuthKey(str);
    	return dto;
    }
    
    @Transactional
    public void updatePassword(String str,String userEmail){
        String pw = encoder.encode(str);
        int id = userRepository.findByEmail(userEmail).getId();
        userRepository.updateUserPassword(id,pw);
    }
    public void mailSend(MailDto mailDto){
        System.out.println("이메일 전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

    public String getTempPassword(){
    	
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    
	public void sendEmailId(String username, String email) {
	        MailDto dto = new MailDto();
	        dto.setAddress(email);
	        dto.setTitle("[Quiz App] 회원님의 ID찾기 안내 메일입니다.");
	        dto.setMessage("안녕하세요. ID찾기 안내 메일 입니다.\n 회원님의 ID는 " + username+" 입니다.\n 감사합니다");
	        mailSend(dto);
	}
}
