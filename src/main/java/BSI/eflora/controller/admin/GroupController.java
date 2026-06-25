package bsi.eflora.controller.admin;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController {

	
	public static class DummyGroup {
        private Long id;
        private String name;
        private LocalDate signupDate;

        public DummyGroup(Long id, String name, LocalDate signupDate) {
            this.id = id;
            this.name = name;
            this.signupDate = signupDate;
        }
        // Getters (Thymeleaf ko data read karne ke liye chahiye)
        public Long getId() { return id; }
        public String getName() { return name; }
        public LocalDate getSignupDate() { return signupDate; }
    }
	
	private static final List<DummyGroup> DUMMY_DATA = new ArrayList<>();
    static {
        DUMMY_DATA.add(new DummyGroup(1L, "Admin Officers Group", LocalDate.of(2024, 8, 12)));
        DUMMY_DATA.add(new DummyGroup(2L, "Technical Editors Desk", LocalDate.of(2025, 2, 14)));
        DUMMY_DATA.add(new DummyGroup(3L, "Field Researchers Team", LocalDate.of(2025, 4, 22)));
        DUMMY_DATA.add(new DummyGroup(4L, "Botanical Survey Reviewers", LocalDate.of(2026, 4, 30)));
        DUMMY_DATA.add(new DummyGroup(5L, "Security Audit Coordinators", LocalDate.of(2024, 12, 4)));
        DUMMY_DATA.add(new DummyGroup(6L, "National Database Managers", LocalDate.of(2025, 1, 8)));
        DUMMY_DATA.add(new DummyGroup(7L, "E-Flora Staging Quality Assurance", LocalDate.of(2025, 9, 15)));
        DUMMY_DATA.add(new DummyGroup(8L, "Lori Lynch Group", LocalDate.of(2026, 4, 12)));
        DUMMY_DATA.add(new DummyGroup(9L, "David Chen Startup", LocalDate.of(2025, 6, 20)));
        DUMMY_DATA.add(new DummyGroup(10L, "Sarah Wilson Acme", LocalDate.of(2024, 11, 8)));
        DUMMY_DATA.add(new DummyGroup(11L, "Mike Johnson Team", LocalDate.of(2024, 7, 30)));
        DUMMY_DATA.add(new DummyGroup(12L, "James Walker IO", LocalDate.of(2025, 3, 18)));
        DUMMY_DATA.add(new DummyGroup(13L, "Olivia Brown Org", LocalDate.of(2026, 5, 1)));
        DUMMY_DATA.add(new DummyGroup(14L, "Ethan Garcia Brand", LocalDate.of(2025, 10, 12)));
        DUMMY_DATA.add(new DummyGroup(15L, "Sophia Lee Firm", LocalDate.of(2025, 2, 28)));
    }
    @GetMapping("/admin/group")
    public String List(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "name,asc") String sort,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {

        // Sort params ko alag karna
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1] : "asc";

        // 🔥 STEP A: Java Streams se Search/Filter lagana
        List<DummyGroup> filteredData = DUMMY_DATA.stream()
                .filter(g -> search.isEmpty() || g.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());

        // 🔥 STEP B: Dynamic Sorting apply karna (In-Memory)
        if ("signupDate".equals(sortField)) {
            filteredData.sort(Comparator.comparing(DummyGroup::getSignupDate));
        } else {
            filteredData.sort(Comparator.comparing(DummyGroup::getName));
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            java.util.Collections.reverse(filteredData);
        }

        // 🔥 STEP C: Pagination (Slice banana)
        int start = Math.min(page * size, filteredData.size());
        int end = Math.min(start + size, filteredData.size());
        List<DummyGroup> pageContent = filteredData.subList(start, end);

        // 🔥 STEP D: Fake Page Object banana taaki Thymeleaf ko asli Page mile
        Pageable pageable = PageRequest.of(page, size);
        Page<DummyGroup> groupsPage = new PageImpl<>(pageContent, pageable, filteredData.size());

        // Model me attributes bind karna
        model.addAttribute("groups", groupsPage);
        model.addAttribute("searchQuery", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        // AJAX Request swap check
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "pages/admin/groups/index :: #table-card-wrapper";
        }

        return "pages/admin/groups/index";
    }
	
	@GetMapping("/admin/group/create")
	public String createForm() {

		return "pages/admin/groups/create";
	}
}
