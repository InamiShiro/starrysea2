package top.starrysea.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.common.ServiceResult;
import top.starrysea.controller.IWorkController;
import top.starrysea.object.dto.Work;
import top.starrysea.object.view.in.WorkForAdd;
import top.starrysea.object.view.in.WorkForAll;
import top.starrysea.object.view.in.WorkForOne;
import top.starrysea.object.view.in.WorkTypeForModify;
import top.starrysea.object.view.in.WorkTypeForRemove;
import top.starrysea.service.IWorkService;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.*;
import static top.starrysea.common.ModelAndViewFactory.*;

@Controller
public class WorkControllerImpl implements IWorkController {

	@Autowired
	private IWorkService workService;

	@Override
	@GetMapping("/work")
	// 查询所有作品，此方法可用于作品管理，也可用于查看旧货
	public Mono<ModelAndView> queryAllWorkController(WorkForAll work, Device device) {
		ServiceResult serviceResult = workService.queryAllWorkService(work.getCondition(), work.toDTO());
		List<Work> result = serviceResult.getResult(LIST_1);
		List<top.starrysea.object.view.out.WorkForAll> voResult = result.stream().map(Work::toVoForAll)
				.collect(Collectors.toList());
		return Mono.justOrEmpty(new ModelAndView(device.isMobile() ? MOBILE + "work" : "work")
				.addObject("result", voResult).addObject("nowPage", serviceResult.getNowPage())
				.addObject("totalPage", serviceResult.getTotalPage()));
	}

	@Override
	@PostMapping("/work/ajax")
	@ResponseBody
	// 查询所有作品，此方法可用于作品管理，也可用于查看旧货
	public Mono<Map<String, Object>> queryAllWorkControllerAjax(@RequestBody WorkForAll work) {
		ServiceResult serviceResult = workService.queryAllWorkService(work.getCondition(), work.toDTO());
		List<Work> result = serviceResult.getResult(LIST_1);
		List<top.starrysea.object.view.out.WorkForAll> voResult = result.stream().map(Work::toVoForAll)
				.collect(Collectors.toList());
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("workName", work.getWorkName());
		theResult.put("result", voResult);
		theResult.put("nowPage", serviceResult.getNowPage());
		theResult.put("totalPage", serviceResult.getTotalPage());
		return Mono.justOrEmpty(theResult);
	}

	@Override
	// 查询一个作品的详情页，此方法可用于作品管理，也可用于查看旧货
	@GetMapping("/work/{workId}")
	public Mono<ModelAndView> queryWorkController(@Valid WorkForOne work, BindingResult bindingResult, Device device) {
		ServiceResult serviceResult = workService.queryWorkService(work.toDTO());
		Work w = serviceResult.getResult(WORK);
		return Mono.justOrEmpty(new ModelAndView(device.isMobile() ? MOBILE + "work_detail" : "work_detail")
				.addObject("work", w.toVoForOne()).addObject("workId", work.getWorkId())
				.addObject("workImages", serviceResult.getResult(LIST_1))
				.addObject("workTypes", serviceResult.getResult(LIST_2)));
	}

	@Override
	// 查询一个作品的详情页，此方法可用于作品管理，也可用于查看旧货
	@PostMapping("/work/detail/ajax")
	@ResponseBody
	public Mono<Map<String, Object>> queryWorkControllerAjax(@RequestBody @Valid WorkForOne work,
			BindingResult bindingResult) {
		ServiceResult serviceResult = workService.queryWorkService(work.toDTO());
		Work w = serviceResult.getResult(WORK);
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("work", w.toVoForOne());
		theResult.put("workId", work.getWorkId());
		theResult.put("workImages", serviceResult.getResult(LIST_1));
		theResult.put("workTypes", serviceResult.getResult(LIST_2));
		return Mono.justOrEmpty(theResult);
	}

	// 添加一个作品
	@PostMapping("/work/add")
	public Mono<ModelAndView> addWorkController(@RequestParam("coverFile") MultipartFile coverFile,
			@RequestParam("imageFiles") MultipartFile[] imageFiles, @Valid WorkForAdd work, BindingResult bindingResult,
			Device device) {
		workService.addWorkService(coverFile, imageFiles, work.toDTO(), work.toDTOWorkType());
		return newSuccessMav("添加成功!", device);
	}

	@Override
	// 删除一个作品
	@PostMapping("/work/remove")
	public Mono<ModelAndView> removeWorkController(@Valid WorkForOne work, BindingResult bindingResult, Device device) {
		workService.removeWorkService(work.toDTO());
		return newSuccessMav("删除成功！", device);
	}

	@Override
	@PostMapping("/worktype/remove")
	public Mono<ModelAndView> removeWorkTypeController(WorkTypeForRemove workType, BindingResult bindingResult,
			Device device) {
		workService.removeWorkTypeService(workType.toDTO());
		return newSuccessMav("删除作品类型成功！", device);
	}

	@Override
	@PostMapping("/worktype/modifystock")
	public Mono<ModelAndView> modifyWorkTypeController(@Valid WorkTypeForModify workType, BindingResult bindingResult,
			Device device) {
		workService.modifyWorkTypeService(workType.toDTO());
		return newSuccessMav("修改库存成功！", device);
	}

}
