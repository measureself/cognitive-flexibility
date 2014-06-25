package rage.ts.controller;

import rage.ts.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListCountController {

    @Autowired
    private ResultService resultService;

    @RequestMapping("app/listcount")
    @ResponseBody
    public int numShownLists(@RequestParam Long userId, @RequestParam String testType, @RequestParam String info) {
        return resultService.getCount(userId, testType, info);
    }
}
