package BSI.eflora.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class DashboardController {

//	@GetMapping("/admin/dashboard")
//	public String dashboard() {
//	    return "pages/admin/dashboard";
//	}
	
	@GetMapping("/admin/dashboard")
	public String dashboard(@RequestHeader(value = "HX-Request", required = false) boolean isHtmx) {
	    if (isHtmx) {
	        // Returns ONLY the dynamic content layout blocks straight inside the #main-content container
	        return "pages/admin/dashboard :: dashboard-body"; 
	    }
	    // Handles a classic top-level browser navigation/refresh by loading everything cleanly
	    return "pages/admin/dashboard"; 
	}
	

	
}
