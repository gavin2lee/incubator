package com.harmazing.springboot;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	@RequestMapping("/health")
	@ResponseBody
	public String health() {
		return "ok";
	}

	@RequestMapping("/hello")
	@ResponseBody
	public User hello() {
		return new User();
	}

	@SuppressWarnings("serial")
	public class User implements Serializable {
		private String name;
		private Date day;
		private String email;

		public User() {
			this.name = "Hello!";
			this.day = new Date();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDay() {
			return day;
		}

		public void setDay(Date day) {
			this.day = day;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}
}
