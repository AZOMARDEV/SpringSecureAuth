package com.mistervoiture.auth.services;

import com.mistervoiture.auth.beans.GPS;
import com.mistervoiture.auth.beans.MetaData;
import com.mistervoiture.auth.entity.Auth;
import com.mistervoiture.auth.entity.Devices;
import com.mistervoiture.auth.entity.Validation;
import com.mistervoiture.auth.enums.BearerID;
import com.mistervoiture.auth.enums.CredentialStatus;
import com.mistervoiture.auth.enums.Roles;
import com.mistervoiture.auth.exceptions.AlreadyExistException;
import com.mistervoiture.auth.exceptions.NotFoundException;
import com.mistervoiture.auth.exceptions.RequestValidationException;
import com.mistervoiture.auth.exceptions.UserNameAlreadyExistException;
import com.mistervoiture.auth.repository.AuthRepository;
import com.mistervoiture.auth.repository.DeviceRepository;
import com.mistervoiture.auth.repository.ValidationRepository;
import com.mistervoiture.auth.request.*;
import com.mistervoiture.auth.response.*;
import com.mistervoiture.auth.security.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AuthServicesImpl implements AuthServices{

    private final ValidationRepository validationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final DeviceRepository deviceRepository;

    public AuthServicesImpl(ValidationRepository validationRepository, PasswordEncoder passwordEncoder, AuthRepository authRepository, JWTUtil jwtUtil, AuthenticationManager authenticationManager, DeviceRepository deviceRepository) {
        this.validationRepository = validationRepository;
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeleteUser deleteUser(String userid, MetaData metadata) {
        Auth auth = authRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found"));
        authRepository.deleteById(auth.get_id());
        return DeleteUser.builder().message("Success").secretname(auth.getSecretNickname()).codeNickname(auth.getCodeNickname()).username(auth.getUsername()).build();
    }

    @Override
    public LoginResp login(LoginReq loginReq, MetaData metadata) {

        Auth auth = authRepository.findAuthByUsername(loginReq.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username or Password Incorrect"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getUsername(),
                        loginReq.getPwd()
                )
        );

        String jwtToken = jwtUtil.generateToken(auth);
        String refreshToken = jwtUtil.generateRefreshToken(auth);

        return LoginResp.builder()
                .credential(auth.getCredential())
                .bearer(auth.getBearerId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .username(auth.getUsername())
                .secretusername(auth.getSecretNickname())
                .build();
    }

    @Override
    public AuthResp registerAuth(AuthReq authReq, MetaData metadata) {

        Optional<Auth> auth = authRepository.findAuthByUsername(authReq.getUsername());
        if (auth.isPresent()) {
            throw new UserNameAlreadyExistException("Username Already Exist");
        }
        String nickname = generateNickNameSuggestions(authReq.getFullname());


        Auth user = authRepository.save(
            Auth.builder()
                    .fullname(authReq.getFullname())
                    .credential(CredentialStatus.localBearer)
                    .username(authReq.getUsername())
                    .pwd(passwordEncoder.encode(authReq.getPwd()))
                    .bearerId(BearerID.MisterVoiture)
                    .secretNickname(nickname)
                    .codeNickname(String.valueOf(UUID.randomUUID()))
                    .role(Roles.USER)
                    .isProfessionalAccount(false)
                    .status(true)
                    .isActive(true)
                    .languageAuth(metadata.getDevice().getLanguage())
                    .AccountNonExpired(true)
                    .CredentialsNonExpired(true)
                    .isPhone(metadata.getDevice().isMobileOnly())
                    .AccountNonLocked(true)
                    .build()
        );

        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthResp.builder()
                .credential(user.getCredential())
                .languageAuth(user.getLanguageAuth())
                .bearerId(user.getBearerId())
                .codeNickname(user.getCodeNickname())
                .nickname(user.getSecretNickname())
                .username(user.getUsername())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public RefreshTokenResp refreshToken(HttpServletRequest request, RefreshTokenReq refreshTokenReq) {
        final String refreshToken = refreshTokenReq.getRefreshToken();
        final String userEmail;

        if (Objects.equals(refreshToken, "") || refreshToken == null) {
            throw new NotFoundException("Token Not Found");
        }

        userEmail = jwtUtil.extractUsernameRefreshToken(refreshToken);
        if (userEmail != null) {
            Auth user = authRepository.findAuthByUsername(userEmail)
                    .orElseThrow();


            /*var isTokenValid = userTokenRepository.findByToken(refreshToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);*/
            var isTokenValid = false;

            if (jwtUtil.isTokenValidRefreshToken(refreshToken, user)) {
                var accessToken = jwtUtil.generateToken(user);

                /*String authHeader = request.getHeader("Authorization");

                String jwt = authHeader.substring(7);*/
                //blackListTokenRepository.save(new BlackListToken(jwt));
                return RefreshTokenResp.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .username(user.getUsername())
                        .build();
            }
        }
        throw new NotFoundException("NO DATA FOUND");
    }

    @Override
    public DeviceId insertDevice(HttpServletRequest request, DeviceReq metaData) {
        Optional<Devices> devices = deviceRepository.GetDevicesByUuid(metaData.getUuid());
        if(devices.isEmpty()){
            devices = Optional.of(deviceRepository.save(
                    Devices.builder()
                            .ip(metaData.getIp())
                            .lng(metaData.getGps().getLng())
                            .lat(metaData.getGps().getLat())
                            .city(metaData.getCity())
                            .continent(metaData.getContinent())
                            .continentCode(metaData.getContinentCode())
                            .countryCode(metaData.getCountryCode())
                            .countryName(metaData.getCountryName())
                            .isBrowser(metaData.isBrowser())
                            .userAgent(metaData.getUserAgent())
                            .uuid(UUID.randomUUID().toString())
                            .language(metaData.getLanguage())
                            .theme("LIGHT")
                            .build()
            ));
        }

        return DeviceId.builder()
                ._id(devices.get().get_id())
                .uuid(devices.get().getUuid())
                .language(devices.get().getLanguage())
                .gps(GPS.builder().lat(devices.get().getLat()).lng(devices.get().getLng()).build())
                .theme(devices.get().getTheme())
                .build();

    }

    @Override
    public int isNickNameUnique(String nickname) {
        return authRepository.isSecretNicknameUnique(nickname);
    }

    @Override
    public int isUserNameUnique(String username) {
        return authRepository.isSecretUsernameUnique(username);
    }

    @Override
    public UserNameUniqueResp userNameUniqueResp(UsernameUniqueReq username) {
        Optional<Auth> auth = authRepository.findAuthByUsername(username.getUsername());
        if (!username.getType().equals("PHONE")){
            if (auth.isPresent()) {
                List<String> usernameSuggestions = generateUsernameSuggestions(username.getFull_name(), username.getFirstname(), username.getLastname());
                return UserNameUniqueResp.builder().is_username_unique(false).usernames(usernameSuggestions).message("Username Already Exist").build();
            }
        }else{
            throw new RequestValidationException("This Is A Phone Number");
        }

        return UserNameUniqueResp.builder()
                .is_username_unique(true).build();
    }

    @Override
    @Transactional
    public UserNameUniqueResp isPhoneValidANDUnique(UsernameUniqueReq username) {

        if (username.getType().equals("PHONE")){
            Optional<Auth> auth = authRepository.findAuthByUsername(username.getUsername());
            if(auth.isPresent()){
                throw new AlreadyExistException("This Phone Number is already Exist");
            }

            Optional<Validation> validation = validationRepository.findAuthByUsername(username.getUsername());

            if(validation.isEmpty()){
                validationRepository.save(
                        Validation.builder()
                                .type(username.getType())
                                .username(username.getUsername())
                                .experationStatus(false)
                                .activation(false)
                                .code("mistervoiture")
                                .experationDate(Validation.AddExperationDate(30))
                                .build());
            }else{
                validationRepository.updateCodeValidation(username.getUsername() , "onedustry");
            }

            /*SendSmsServices2.SendSms(DataBean.builder().recipient(username.getUsername())
                    .message("Hello " + username.getLastname() + " " + username.getFirstname() +",\n" +
                            "\n" +
                            "Welcome to MisterVoiture! To activate your account, please use the following verification code:\n" +
                            "\n" +
                            "Verification Code: mistervoiture\n" +
                            "\n" +
                            "Enter this code on our website or app to complete the activation process. If you didn't request this code, please ignore this message.\n" +
                            "\n" +
                            "Thank you for choosing MisterVoiture!\n" +
                            "\n" +
                            "Best regards,\n" +
                            "MisterVoiture\n")
                    .build());

            SendWhatsappServices2.SendMessageWhatsapp(DataBean.builder().recipient(username.getUsername()).build());*/

            return UserNameUniqueResp.builder()
                    .is_username_unique(true).build();
        }else{
            throw new RequestValidationException("This Is A Email Or UserName");
        }

    }

    @Override
    @Transactional
    public UserNameUniqueResp validCodePhone(ValidationCodePhoneNumber validationCodePhoneNumber) {
        Validation validation = validationRepository.findAuthByUsername(validationCodePhoneNumber.getPhone())
                .orElseThrow(() ->  new NotFoundException("Username NotFound") );

        if(validation.getCode().equals(validationCodePhoneNumber.getCode())){
            validationRepository.updateStatusValidation(validation.getUsername());
            return UserNameUniqueResp.builder()
                    .is_username_unique(true).build();
        }
        throw new RequestValidationException("Incorrect Code");
    }

    @Override
    @Transactional
    public Response UpdateSecurityPass(HttpServletRequest request, UpdateSecurityPwdReq updateSecurityPwdReq) {
        String authHeader = request.getHeader("Authorization");

        String jwt = authHeader.substring(7);
        log.info("jwt " + jwt);
        String userEmail = jwtUtil.extractUsername(jwt);
        log.info("userEmail " + userEmail);

        Auth auth = authRepository.findAuthByUsername(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Exist"));

        String old_pass = passwordEncoder.encode(updateSecurityPwdReq.getOld_pwd());
        log.error("pass match " + auth.getPassword());


        if(!passwordEncoder.matches(updateSecurityPwdReq.getOld_pwd(), auth.getPassword())) throw new RequestValidationException("Old Password is incorrect");
        log.error("pass match " + passwordEncoder.matches(auth.getPassword() , old_pass));
        log.error("pass match " + passwordEncoder.matches( updateSecurityPwdReq.getOld_pwd(), auth.getPassword()));

        log.info("pass " + old_pass);
        log.info("_id " + auth.get_id());
        authRepository.updateSecurityPass(auth.get_id() , passwordEncoder.encode(updateSecurityPwdReq.getNew_pwd()));

        return Response.builder().message("Update Success").status(true).build();
    }

    /*******************************************************************/

    public List<String> generateUsernameSuggestions(String fullName , String firstName , String lastName) {
        List<String> suggestions = new ArrayList<>();
        Random random = new Random();
        // Split the full name into first name and last name parts

        List<String> UsernameTypeGenerated = new ArrayList<>(Arrays.asList(".","_","-"));

        UsernameTypeGenerated
                .forEach(usernameType -> {
                    StringBuilder Username = new StringBuilder((firstName + usernameType + lastName).replaceAll("\\s+", ""));
                    if(isUserNameUnique(Username.toString()) == 0) suggestions.add(Username.toString());

                    boolean isUsernameUnique = false;
                    do {
                        Username.append(usernameType).append(random.nextInt(1000));
                        if(isNickNameUnique(Username.toString()) == 0){
                            isUsernameUnique = true;
                            suggestions.add(Username.toString());
                        }
                    } while (!isUsernameUnique);

                    if (firstName.length() <= 3) {
                        Username = new StringBuilder((firstName.charAt(1) + usernameType + lastName).replaceAll("\\s+", ""));
                        if(isUserNameUnique(Username.toString()) == 0) suggestions.add(Username.toString());

                        isUsernameUnique = false;
                        do {
                            Username.append(usernameType).append(random.nextInt(1000));
                            if(isNickNameUnique(Username.toString()) == 0){
                                isUsernameUnique = true;
                                suggestions.add(Username.toString());
                            }
                        } while (!isUsernameUnique);
                    }

                    String[] lastNameParts = lastName.split("\\s+"); // Split last name by spaces

                    if (lastNameParts.length >= 2 && lastNameParts.length <= 3) {
                        String suggestedUsername = String.join(".", lastNameParts);

                        String username = (firstName + usernameType + suggestedUsername).replaceAll("\\s+", "");
                        if(isUserNameUnique(username) == 0) suggestions.add(username);

                    } else if (lastName.length() > 6) {
                        String lastNamePart1 = lastName.substring(0, 3);
                        String lastNamePart2 = lastName.substring(3);

                        String username = (firstName + usernameType + lastNamePart1 + usernameType + lastNamePart2).replaceAll("\\s+", "");
                        if(isUserNameUnique(username) == 0) suggestions.add(username);

                    }

                });

        return suggestions;
    }

    public String generateNickNameSuggestions(String fullName) {

        Random random = new Random();
        // Split the full name into first name and last name parts
        String[] nameParts = fullName.toLowerCase().split("\\s+");

        String firstName = nameParts[0];
        String lastName = (nameParts.length < 2) ? "mv" : String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));;

        String nickname = (firstName + "." + lastName).replaceAll("\\s+", "");
        boolean isNicknameUnique = false;

        isNicknameUnique = (isNickNameUnique(nickname) == 0);

        if(!isNicknameUnique){
            do {
                nickname = (nickname + "." + random.nextInt(1000)).replaceAll("\\s+", "");
                isNicknameUnique = (isNickNameUnique(nickname) == 0);
            } while (!isNicknameUnique);
        }

        return nickname;
    }
}
