package com.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminTransferFilterRequest {

    public enum SearchType { ALL, SENDER, RECEIVER }
    public enum KeywordType { EMAIL, ACCOUNT}

    private SearchType searchType = SearchType.ALL;
    private KeywordType keywordType = KeywordType.EMAIL;
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;

}
