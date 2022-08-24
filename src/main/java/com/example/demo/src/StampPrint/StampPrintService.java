package com.example.demo.src.StampPrint;

import com.example.demo.config.BaseException;
import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.StampPrint.model.PostStampReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.PAGES_CANNOT_DELETE_TOP_PAGE;

@Service

public class StampPrintService {
    private final StampPrintDao stampPrintDao;

    @Autowired
    public StampPrintService(StampPrintDao stampPrintDao) {
        this.stampPrintDao = stampPrintDao;
    }

    public Boolean stampBlock(PostStampReq postStampReq) throws BaseException {
        try {
            stampPrintDao.stampBlock(postStampReq);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*public Boolean updateToPrint(int blockId) throws BaseException {
        try {
            stampPrintDao.updateToPrint(blockId);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }*/
}
