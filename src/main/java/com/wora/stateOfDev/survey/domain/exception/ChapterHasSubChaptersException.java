package com.wora.stateOfDev.survey.domain.exception;

import com.wora.stateOfDev.survey.domain.valueObject.ChapterId;

public class ChapterHasSubChaptersException extends RuntimeException {
    public ChapterHasSubChaptersException(ChapterId chapterId) {
        super("Cannot create question with chapter that has sub chapters");
    }
}
