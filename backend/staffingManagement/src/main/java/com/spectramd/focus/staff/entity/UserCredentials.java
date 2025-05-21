/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author raghav.mittal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    private Integer userId;
    private String email;
    private String password;
    private Integer employeeId;
    private Boolean isAdmin;

}
