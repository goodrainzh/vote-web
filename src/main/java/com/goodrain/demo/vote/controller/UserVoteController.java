package com.goodrain.demo.vote.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.goodrain.demo.vote.service.UserService;
import com.goodrain.demo.vote.service.VoteService;

@Controller
public class UserVoteController {
	@Autowired
	private UserService userService;
	@Autowired
	private VoteService voteService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/users/register")
	public String registerUser(@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {

		if (name.length() == 0) {
			name = randomName();
		}

		Integer userId = userService.registerUser(name);

		model.addAttribute("userId", userId);
		model.addAttribute("name", name);
		model.addAttribute("topic", "好雨云平台,助力DevOps!");

		return "votePage";
	}

	private String randomName() {
		Random r = new Random();
		return "游客" + r.nextInt(Integer.MAX_VALUE);
	}

	@RequestMapping("/vote")
	public String voteTopic(@RequestParam("userId") Integer userId, @RequestParam("topic") String topic,
			@RequestParam("isAgree") String isAgree, Model model) {

		String userName = "";
		if (userId == null) {
			userName = randomName();
			userId = userService.registerUser(userName);
		} else {
			userName = userService.getUser(userId);
		}

		String voteMsg = "";
		if (isAgree == null) {
			voteMsg = "您没有选择态度!";
		} else {
			if (isAgree.trim().length() > 0 && isAgree.equals("0")) {
				voteMsg = voteService.disagree(userName, topic);
			} else {
				voteMsg = voteService.agree(userName, topic);
			}
		}

		model.addAttribute("userId", userId);
		model.addAttribute("name", userName);
		model.addAttribute("voteMsg", voteMsg);
		model.addAttribute("topic", topic);

		return "votePage";
	}

	@RequestMapping("/vote/agree/{userId}/{topic}")
	public void voteTopicAgree(@PathVariable("userId") Integer userId, @PathVariable("topic") String topic,
			Model model) {
		String userName = userService.getUser(userId);
		if (userName == null || userName.length() == 0) {
			return;
		}
		voteService.agree(userName, topic);
	}

	@RequestMapping("/vote/disagree/{userId}/{topic}")
	public void voteTopicDisagree(@PathVariable("userId") Integer userId, @PathVariable("topic") String topic,
			Model model) {
		String userName = userService.getUser(userId);
		if (userName == null || userName.length() == 0) {
			return;
		}
		voteService.disagree(userName, topic);
	}

}
