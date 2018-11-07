package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword() {
		return "update-password";
	}

	@RequestMapping(value = "/submitPassword", method = RequestMethod.POST)
	private ModelAndView updatePasswordSubmit(@ModelAttribute PasswordModel pass, Model model,
			RedirectAttributes redirect) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService
				.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String msg = "";

		if (pass.getConfirm().equals(pass.getNewPass())) {
			if (passwordEncoder.matches(pass.getOld(), user.getPassword())) {
				userService.changePassword(user, pass.getNewPass());
				msg = "Password berhasil diubah!";
			} else {
				msg = "Password lama anda salah!";
			}
		} else {
			msg = "Password baru tidak sesuai!";
		}
		ModelAndView modelAndView = new ModelAndView("redirect:/user/updatePassword");
		redirect.addFlashAttribute("msg", msg);
		return modelAndView;
	}

}
