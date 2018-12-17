package top.starrysea.common;

import static top.starrysea.common.Const.MOBILE;
import static top.starrysea.common.Const.SUCCESS_VIEW;
import static top.starrysea.common.Const.ERROR_VIEW;
import static top.starrysea.common.Const.INFO;
import static top.starrysea.common.Const.ERRINFO;

import org.springframework.mobile.device.Device;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;

public class ModelAndViewFactory {

	private ModelAndViewFactory() {
	}

	public static Mono<ModelAndView> newSuccessMav(String info, Device device) {
		ModelAndView modelAndView = new ModelAndView(device.isMobile() ? MOBILE + SUCCESS_VIEW : SUCCESS_VIEW);
		modelAndView.addObject(INFO, info);
		return Mono.justOrEmpty(modelAndView);
	}

	public static Mono<ModelAndView> newErrorMav(String errInfo, Device device) {
		ModelAndView modelAndView = new ModelAndView(device.isMobile() ? MOBILE + ERROR_VIEW : ERROR_VIEW);
		modelAndView.addObject(ERRINFO, errInfo);
		return Mono.justOrEmpty(modelAndView);
	}
}
