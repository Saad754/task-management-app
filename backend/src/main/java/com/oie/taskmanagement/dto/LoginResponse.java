package com.oie.taskmanagement.dto;

public record LoginResponse (
     String token,
     UserResponse user
){}
