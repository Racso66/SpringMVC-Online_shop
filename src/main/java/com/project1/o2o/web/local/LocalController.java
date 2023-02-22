package com.project1.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Set page mapping under /local
 */
@Controller
@RequestMapping("/local")
public class LocalController {
	/*
	 * bind account page
	 */
	@RequestMapping(value = "/accountbind", method = RequestMethod.GET)
	private String accountbind() {
		return "local/accountbind";
	}
	/*
	 * login page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	private String login() {
		return "local/login";
	}
	/*
	 * change password page
	 */
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	private String changepwd() {
		return "local/changepwd";
	}
}
