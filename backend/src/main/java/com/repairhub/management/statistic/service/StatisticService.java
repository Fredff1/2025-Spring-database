package com.repairhub.management.statistic.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

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
import com.repairhub.management.statistic.repository.CostAnalysisRepository;
import com.repairhub.management.statistic.repository.FeedbackStatisticRepository;
import com.repairhub.management.statistic.repository.RepairOrderStatisticRepository;
import com.repairhub.management.statistic.repository.VehicleStatisticRepository;

@Service
public class StatisticService {
    private final FeedbackStatisticRepository feedbackStatisticRepository;
    private final CostAnalysisRepository costAnalysisRepository;
    private final VehicleStatisticRepository vehicleStatisticRepository;
    private final RepairOrderStatisticRepository repairOrderStatisticRepository;

    public StatisticService(
            FeedbackStatisticRepository feedbackStatisticRepository,
            CostAnalysisRepository costAnalysisRepository,
            VehicleStatisticRepository vehicleStatisticRepository,
            RepairOrderStatisticRepository repairOrderStatisticRepository
        ) {
        this.feedbackStatisticRepository = feedbackStatisticRepository;
        this.costAnalysisRepository = costAnalysisRepository;
        this.vehicleStatisticRepository = vehicleStatisticRepository;
        this.repairOrderStatisticRepository = repairOrderStatisticRepository;
    }

    public PageResponse<NegativeFeedbackStatDTO> getAllNegative(int page,int limit) {
        return feedbackStatisticRepository.findAllNegativeFeedbacks(page,limit);
    }

    public PageResponse<NegativeFeedbackStatDTO> getRecentNegative(int page,int limit) {
        return feedbackStatisticRepository.findNegativeFeedbacksSince(LocalDateTime.now().minusMonths(1L),page,limit);
    }

    /**
     * 全时间成本分析
     */
    public CostAnalysisDTO getCostAnalysisAllTime() {
        BigDecimal matCost = costAnalysisRepository.findTotalMaterialCostAllTime();
        BigDecimal labCost = costAnalysisRepository.findTotalLaborCostAllTime();
        return CostAnalysisDTO.from(matCost, labCost, "all");
    }

    /**
     * 指定月度成本分析，传入 YearMonth，例如 YearMonth.of(2025, 6)
     */
    public CostAnalysisDTO getCostAnalysisByMonth() {
        LocalDateTime since = LocalDateTime.now().minusMonths(1L);
        BigDecimal matCost = costAnalysisRepository.findMaterialCostByTime(since);
        BigDecimal labCost = costAnalysisRepository.findLaborCostByTime(since);
        return CostAnalysisDTO.from(matCost, labCost, since.toString());
    }

    /**
     * 获得所有类型车辆的已完成的订单次数和费用
     * @return
     */
    public List<VehicleRepairStatsDTO> getAllVehicleRepairStats(){
        return vehicleStatisticRepository.findAllVehicleRepairStats();
    }

    /**
     * 获得一个月内所有类型车辆的已完成的订单次数和费用
     * @return
     */
    public List<VehicleRepairStatsDTO> getVehicleRepairStatsByTime(){
        LocalDate since = LocalDate.now().minusMonths(1L);
        return vehicleStatisticRepository.findVehicleRepairStatsByTime(since);
    }

    /**
     * 获得所有类型车辆的所有不同fault_type对应的订单次数
     * @return
     */
    public List<VehicleModelFaultTypeCountDTO> getAllFaultTypeCount(){
        return vehicleStatisticRepository.findAllFaultTypeCount();
    }

    /**
     * 获得一个月内类型车辆的所有不同fault_type对应的订单次数
     * @return
     */
    public List<VehicleModelFaultTypeCountDTO> getAllFaultTypeCountByTime(){
        LocalDate since = LocalDate.now().minusMonths(1L);
        return vehicleStatisticRepository.findFaultTypeCountByTime(since);
    }

    public List<OrderProcessStatDTO> getOrderProcessStatDTO(
        LocalDate begin,
        LocalDate end
    ){
        return repairOrderStatisticRepository.findProcessStatDTO(begin, end);
    }

    public List<MismatchRepairTypeDTO> getMismatchRecord(){
        return repairOrderStatisticRepository.findMismatchRecord();
    }

    public List<UnfinishedOrderFaultTypeStatsDTO> getUnfinishedOrderFaultTypeStats(){
        return repairOrderStatisticRepository.findUnfinishedOrderFaultTypeStats();
    }

    public List<UnfinishedRepairmanStatsDTO> getUnfinishedOrderRepairmanStats(){
        return repairOrderStatisticRepository.findUnfinishedOrderRepairmanStats();
    }

    public List<UnfinishedVehicleStatsDTO> getUnfinishedOrderVehicleStats(){
        return repairOrderStatisticRepository.findUnfinishedOrderVehicleStats();
    }
}
