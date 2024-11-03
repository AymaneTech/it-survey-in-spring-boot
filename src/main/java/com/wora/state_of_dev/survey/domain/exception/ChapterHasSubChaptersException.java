package com.wora.state_of_dev.survey.domain.exception;

import com.wora.state_of_dev.survey.domain.valueObject.ChapterId;

public class ChapterHasSubChaptersException extends RuntimeException {
    public ChapterHasSubChaptersException(ChapterId chapterId) {
        super("Cannot create question with chapter that has sub chapters");
    }
}
