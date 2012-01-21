package com.dubious.itunes.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dubious.itunes.statistics.exception.StatisticsException;
import com.dubious.itunes.statistics.service.SnapshotSynchronizeService;

/**
 * Controller for invoking snapshot synchronization.
 */
@Controller
public class SnapshotSynchronizeController {

    @Resource(name = "fileToMongoDbSnapshotSynchronizeService")
    private SnapshotSynchronizeService snapshotSynchronizeService;

    /**
     * Synchronize snapshots.
     * 
     * @return View.
     * @throws StatisticsException On unexpected error.
     */
    @RequestMapping(value = "/snapshot/snapshotSynchronize.do", method = RequestMethod.GET)
    public final ModelAndView synchronizeSnapshots() throws StatisticsException {

        snapshotSynchronizeService.synchronizeSnapshots();
        return new ModelAndView("redirect:/snapshot/snapshotSynchronizeDone.jsp");
    }
}
