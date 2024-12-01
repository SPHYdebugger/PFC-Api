package com.sphy.PFC_Api.controller;


import com.sphy.PFC_Api.dto.UserDTO;
import com.sphy.PFC_Api.dto.VehicleDTO;
import com.sphy.PFC_Api.exception.UserAlreadyExistException;
import com.sphy.PFC_Api.exception.UserNotFoundException;
import com.sphy.PFC_Api.exception.VehicleAlreadyExistException;
import com.sphy.PFC_Api.exception.VehicleNotFoundException;
import com.sphy.PFC_Api.model.ErrorResponse;
import com.sphy.PFC_Api.model.User;
import com.sphy.PFC_Api.model.Vehicle;
import com.sphy.PFC_Api.repository.UserRepository;
import com.sphy.PFC_Api.service.RefuelService;
import com.sphy.PFC_Api.service.UserService;
import com.sphy.PFC_Api.service.VehicleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserController.class);




    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserDTObyId(@PathVariable String email) throws UserNotFoundException {
        System.out.println("entra en mail");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        UserDTO dto = new UserDTO();

        dto.setPassword(user.getPassword());
        dto.setUsername(user.getUsername());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/userDTO/{id}")
    public ResponseEntity<UserDTO> getUsernameDTObyId(@PathVariable long id) throws UserNotFoundException {
        System.out.println("llega a id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    // Añadir un nuevo vehículo
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User newUser) {
        System.out.println("entra por register");
        Optional<User> optionalUser = userRepository.findByEmail(newUser.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("A user with this email already exists.");
        }
        String password = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(password);
        newUser.setCreationDate(LocalDate.now());
        User savedUser = userService.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }









    // Control de excepciones
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistException(UserAlreadyExistException ex) {
        ErrorResponse errorResponse = ErrorResponse.generalError(409, ex.getMessage());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}