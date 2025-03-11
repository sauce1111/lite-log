document.addEventListener("DOMContentLoaded", function () {
    let calendarEl = document.getElementById("calendar");

    let calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: "dayGridMonth",
        locale: "ko",
        headerToolbar: {
            left: "prev,next today",
            center: "title",
            right: "dayGridMonth,timeGridWeek,timeGridDay"
        },
        events: function (fetchInfo, successCallback, failureCallback) {
            fetch("/diary/calendar")
                .then(response => response.json())
                .then(responseData => {
                    if (!responseData.data) {
                        console.error("다이어리 데이터가 없습니다. :", responseData)
                        failureCallback("No Data");
                        return;
                    }
                    let events = responseData.data.map(diary => ({
                        title: diary.title,
                        start: diary.date,
                        url: `/diary/${diary.id}`
                    }));
                    successCallback(events);
                })
                .catch(error => {
                    console.error("Error fetching diaries:", error);
                    failureCallback(error);
                });
        },
        dateClick: function (info) {
            window.location.href = `/diary/new?date=${info.dateStr}`;
        }
    });

    calendar.render();
});
