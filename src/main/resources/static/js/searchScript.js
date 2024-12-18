document.addEventListener("DOMContentLoaded", function () {
    const searchForm = document.getElementById("searchForm");
    const queryInput = document.querySelector(".search-input");

    // 검색 버튼을 클릭하면 현재 카테고리와 이벤트 값을 함께 제출
    searchForm.addEventListener("submit", function (event) {
        const categoryInput = document.getElementById("categoryInput");
        const eventInput = document.getElementById("eventInput");
        categoryInput.value = categoryInput.value || "";
        eventInput.value = eventInput.value || "";
    });

    // 검색어 입력 후 Enter 키를 누를 때도 상태 유지
    queryInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            searchForm.submit();
        }
    });
});