package com.repairhub.management.admin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.statistic.dto.CostAnalysisDTO;
import com.repairhub.management.statistic.dto.MismatchRepairTypeDTO;
import com.repairhub.management.statistic.dto.NegativeFeedbackStatDTO;
import com.repairhub.management.statistic.dto.OrderProcessStatDTO;
import com.repairhub.management.statistic.dto.UnfinishedOrderFaultTypeStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedRepairmanStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedVehicleStatsDTO;
import com.repairhub.management.statistic.dto.VehicleModelFaultTypeCountDTO;
import com.repairhub.management.statistic.dto.VehicleRepairStatsDTO;
import com.repairhub.management.statistic.service.StatisticService;
import com.repairhub.management.utils.PageUtils;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatisticController {
    
    private final StatisticService statisticService;

    public AdminStatisticController(
        StatisticService statisticService
    ){
        this.statisticService = statisticService;
    }

    @GetMapping("/feedback/negative")
    public ResponseEntity<PageResponse<NegativeFeedbackStatDTO>> getNegativeStat(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "10")   int limit,
        @RequestParam(defaultValue = "recent")  String period
    ){  
        List<NegativeFeedbackStatDTO> dtos = null;
        if(period.equals("recent")){
            dtos = statisticService.getRecentNegative();
        }else{
            dtos = statisticService.getAllNegative();
        }
        List<NegativeFeedbackStatDTO> pagedDto = PageUtils.paginate(dtos, page, limit);
        PageResponse<NegativeFeedbackStatDTO> response = new PageResponse<>(pagedDto, dtos.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cost/proportion")
    public ResponseEntity<CostAnalysisDTO> getCostStat(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "10")   int limit,
        @RequestParam(defaultValue = "recent")  String period
    ){  
        CostAnalysisDTO dto = null;
        if(period.equals("recent")){
            dto = statisticService.getCostAnalysisByMonth();
        }else{
            dto = statisticService.getCostAnalysisAllTime();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/vehicle/order-stat")
    public ResponseEntity<List<VehicleRepairStatsDTO>> getVehicleRepairStats(
        @RequestParam(defaultValue = "recent")  String period) {
        List<VehicleRepairStatsDTO> dtos = null;
        if(period.equals("recent")){
            dtos = statisticService.getVehicleRepairStatsByTime();
        }else{
            dtos = statisticService.getAllVehicleRepairStats();
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/vehicle/fault-type")
    public ResponseEntity<List<VehicleModelFaultTypeCountDTO>> getFaultTypeCount(
       @RequestParam(defaultValue = "recent")  String period) {
        List<VehicleModelFaultTypeCountDTO> dtos = null;
        if(period.equals("recent")){
            dtos = statisticService.getAllFaultTypeCountByTime();
        }else{
            dtos = statisticService.getAllFaultTypeCount();
        }
        return ResponseEntity.ok(dtos);
    }

    /**
     * 统计不同类型的order已完成和未完成占比
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/repair-orders/process")
    public ResponseEntity<List<OrderProcessStatDTO>> getOrderProcessStatDTO(
        @RequestParam LocalDate begin,
        @RequestParam LocalDate end
    ){
        var dtos = statisticService.getOrderProcessStatDTO(begin, end);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/repair-orders/mismatch")
    public ResponseEntity<List<MismatchRepairTypeDTO>> getOrderMismatchRecord(){
        var dtos = statisticService.getMismatchRecord();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/repair-orders/unfinished/fault-type")
    public ResponseEntity<List<UnfinishedOrderFaultTypeStatsDTO>> getUnfinishedOrderFaultTypeStats(){
        var dtos = statisticService.getUnfinishedOrderFaultTypeStats();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/repair-orders/unfinished/repairman")
    public ResponseEntity<List<UnfinishedRepairmanStatsDTO>> getUnfinishedOrderRepairmanStats(){
        var dtos = statisticService.getUnfinishedOrderRepairmanStats();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/repair-orders/unfinished/vehicle")
    public ResponseEntity<List<UnfinishedVehicleStatsDTO>> getUnfinishedOrderVehicleStats(){
        var dtos = statisticService.getUnfinishedOrderVehicleStats();
        return ResponseEntity.ok(dtos);
    }
    
}
