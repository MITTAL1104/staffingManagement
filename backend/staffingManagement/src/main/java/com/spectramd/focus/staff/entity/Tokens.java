/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spectramd.focus.staff.entity;

import java.util.Date;
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
public class Tokens {

    private Integer tokenId;
    private Integer employeeId;
    private String token;
    private Date issuedAt;
    private Date expiry;
}
