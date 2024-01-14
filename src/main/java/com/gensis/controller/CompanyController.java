package com.gensis.controller;

import com.gensis.model.Company;
import com.gensis.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @Operation(summary = "Retrieve all Companies", tags = "Company")
    @GetMapping
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    @Operation(summary = "Find a Company by VAT", tags = "Company")
    @GetMapping("{vat}")
    public ResponseEntity<Company> getCompanyByVat(@PathVariable String vat) {
        return companyRepository.findAll().stream().filter(company -> company.getVat().equals(vat))
                .map(company -> new ResponseEntity<>(company, HttpStatus.OK)).findAny()
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a new Company", tags = "Company")
    @PostMapping
    public Company createCompany(@RequestBody Company Company) {
        return companyRepository.save(Company);
    }

    @Operation(summary = "Update a Company", tags = "Company")
    @PutMapping("/{vat}")
    public ResponseEntity<Company> updateCompany(@PathVariable String vat, @RequestBody Company updatedCompany) {
        return companyRepository.findAll().stream().filter(company -> company.getVat().equals(vat))
                .map(company -> {
                    company.setAddress(updatedCompany.getAddress());
                    company.setVat(updatedCompany.getVat());

                    return new ResponseEntity<>(companyRepository.save(company), HttpStatus.OK);
                }).findAny()
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Add a Contact to Company", tags = "Company")
    @PutMapping("/{vat}/{id}")
    public ResponseEntity<Company> addEmployeeToCompany(@PathVariable Map<String, String> pathVarsMap) {
        String vat = pathVarsMap.get("vat");
        Long id = Long.parseLong(pathVarsMap.get("id"));
        return companyRepository.findAll().stream().filter(company -> company.getVat().equals(vat))
                .map(company -> {
                    company.setEmployeeList(id);
                    return new ResponseEntity<>(companyRepository.save(company), HttpStatus.OK);
                }).findAny()
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a Company by Id", tags = "Company")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
