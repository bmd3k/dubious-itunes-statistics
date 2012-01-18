package com.dubious.itunes.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.dubious.itunes.statistics.service.SnapshotSynchronizeService;

public class SnapshotSynchronizeController implements Controller {

    private SnapshotSynchronizeService snapshotSynchronizeService;

    public SnapshotSynchronizeController(SnapshotSynchronizeService snapshotSynchronizeService) {
        this.snapshotSynchronizeService = snapshotSynchronizeService;
    }

    // TODO: use Spring annotations

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        snapshotSynchronizeService.synchronizeSnapshots();
        return new ModelAndView("redirect:/snapshot/snapshotSynchronizeDone.jsp");
    }
}
