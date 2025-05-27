package models;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
  private String email;
  private String password;
  private String name;
}
