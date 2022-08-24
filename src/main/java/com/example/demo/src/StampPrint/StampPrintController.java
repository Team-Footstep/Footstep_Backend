package com.example.demo.src.StampPrint;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.StampPrint.model.PostStampReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class StampPrintController {

    private StampPrintProvider stampPrintProvider;
    private StampPrintService stampPrintService;

    @Autowired
    public StampPrintController(StampPrintProvider stampPrintProvider, StampPrintService stampPrintService){
        this.stampPrintProvider = stampPrintProvider;
        this.stampPrintService = stampPrintService;
    }

    /*
     * [POST]
     * stamp
     * */
    @PostMapping("/stamp")
    public BaseResponse<Boolean> stampBlock(@RequestBody PostStampReq postStampReq)  {
        try{
            return new BaseResponse<>(stampPrintService.stampBlock(postStampReq));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
     * [PATCH]
     * stamp to print
     * */
//    @PostMapping("/print/{blockId}")
//    public BaseResponse<Boolean> updateToPrint(@PathVariable int blockId)  {
//        try{
//            return new BaseResponse<>(stampPrintService.updateToPrint(blockId));
//        }catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

}
