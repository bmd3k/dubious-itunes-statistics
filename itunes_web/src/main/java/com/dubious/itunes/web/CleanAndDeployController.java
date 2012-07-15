package com.dubious.itunes.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.store.CleanAndDeploy;

/**
 * Controller for cleaning and deploying the MongoDB.
 */
@Controller
public class CleanAndDeployController {

    @Resource(name = "mongoDbCleanAndDeploy")
    private CleanAndDeploy cleanAndDeploy;

    /**
     * Clean And Deploy.
     * 
     * @return View.
     * @throws StatisticsException On unexpected error.
     */
    @RequestMapping(value = "/cleanAndDeploy/cleanAndDeploy.do", method = RequestMethod.GET)
    public final ModelAndView synchronizeSnapshots() throws StatisticsException {

        cleanAndDeploy.cleanAndDeploy();
        return new ModelAndView("redirect:/cleanAndDeploy/cleanAndDeployDone.jsp");
    }
}
