/*jshint esversion: 8 */
/* 이용 신청서 */

function checkApplyForm() {
    let phoneReg = /\d\d\d\d/;

    let name = document.getElementById('name');
    let email = document.getElementById('email');
    let phone1 = document.getElementById('phone1');
    let phone2 = document.getElementById('phone2');
    let phone3 = document.getElementById('phone3');

    if (phone1.value != '010') {
        alert('휴대전화번호를 잘못 입력하셨습니다.');
        return false;
    }

    if (!phoneReg.exec(phone2.value) || !phoneReg.exec(phone3.value)) {
        alert("휴대전화번호를 잘못 입력하셨습니다.");
        return false;
    }

    let voteTitle = document.getElementById('voteTitle');

    if (!voteTitle.value) {
        alert("제목을 입력하세요");
        return false;
    }
    return true;
}

/* 이용 신청서 등록 */
//각각의 검증은 나중에
async function applySubmit() {
    if (!checkApplyForm()) {
        return;
    }

    let name = document.getElementById('name').value;
    let email = document.getElementById('email').value;
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;
    let voteTitle = document.getElementById('voteTitle').value;
    let expectedCount = document.getElementById('expectedCount').value;
    let startYear = document.getElementById('startYear').value;
    let startMonth = document.getElementById('startMonth').value;
    let startDay = document.getElementById('startDay').value;
    let startHour = document.getElementById('startHour').value;
    let startMinute = document.getElementById('startMinute').value;

    let endYear = document.getElementById('endYear').value;
    let endMonth = document.getElementById('endMonth').value;
    let endDay = document.getElementById('endDay').value;
    let endHour = document.getElementById('endHour').value;
    let endMinute = document.getElementById('endMinute').value;

    let startVote = `${startYear}-${startMonth}-${startDay}T${startHour}:${startMinute}:00`;
    let endVote = `${endYear}-${endMonth}-${endDay}T${endHour}:${endMinute}:00`;

    let startDate = new Date(startYear, Number(startMonth) + 1, startDay, startHour, startMinute, 0);
    let endDate = new Date(endYear, Number(endMonth) + 1, endDay, endHour, endMinute, 0);
    let now = new Date();

    if (startDate < now) {
        alert('오늘보다 이전의 값으로 설정 불가');
        return;
    }

    if (startDate > endDate) {
        alert('시작날짜는 종료날짜보다 항상 앞에 있어야 합니다.');
        return;
    }

    const value = {
        name,
        email,
        phone: phone1 + phone2 + phone3,
        voteTitle,
        expectedCount: expectedCount,
        startVote,
        endVote
    };

    let response = await fetch('/api/v1/apply', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(value)
    });

    if (response && response.ok) {
        window.location.replace('/apply');
    } else {
        alert(await response.text());
    }

}

function sameInfo(e) {
    fetch('/api/v1/user/info').then((response) => response.json()).then(data => {
        document.getElementById('name').value = data.name;
        document.getElementById('email').value = data.email;
        document.getElementById('phone1').value = data.phone.substring(0, 3);
        document.getElementById('phone2').value = data.phone.substring(3, 7);
        document.getElementById('phone3').value = data.phone.substring(7, 11);
    }).catch((error) => {
        console.log(error);
    });

    e.checked = false;
}

//각각의 검증은 나중에
async function modifyApply(applyId) {

    let name = document.getElementById('name').value;
    let email = document.getElementById('email').value;
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;
    let voteTitle = document.getElementById('voteTitle').value;
    let expectedCount = document.getElementById('expectedCount').value;
    let startYear = document.getElementById('startYear').value;
    let startMonth = document.getElementById('startMonth').value;
    let startDay = document.getElementById('startDay').value;
    let startHour = document.getElementById('startHour').value;
    let startMinute = document.getElementById('startMinute').value;

    let endYear = document.getElementById('endYear').value;
    let endMonth = document.getElementById('endMonth').value;
    let endDay = document.getElementById('endDay').value;
    let endHour = document.getElementById('endHour').value;
    let endMinute = document.getElementById('endMinute').value;

    let startVote = `${startYear}-${startMonth}-${startDay}T${startHour}:${startMinute}:00`;
    let endVote = `${endYear}-${endMonth}-${endDay}T${endHour}:${endMinute}:00`;

    let startDate = new Date(startYear, Number(startMonth) - 1, startDay, Number(startHour), Number(
        startMinute), 0);
    let endDate = new Date(endYear, Number(endMonth) - 1, endDay, Number(endHour), Number(
        endMinute), 0);
    let now = new Date();

    if (startDate < now) {
        alert('오늘보다 이전의 값으로 설정 불가');
        return;
    }

    if (startDate > endDate) {
        alert('시작날짜는 종료날짜보다 항상 앞에 있어야 합니다.')
        return;
    }
    const value = {
        id: applyId,
        name,
        email,
        phone: phone1 + phone2 + phone3,
        voteTitle,
        expectedCount,
        startVote,
        endVote
    }

    let response = await fetch('/api/v1/apply', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(value)
    });

    if (response && response.ok) {
        alert("성공");
    } else {
        console.log(error);
        alert('에러@@');
    }

    window.location.reload();

}

/* 이용신청서 수정 화면 설정*/
async function modifyApplyView(applyId) {
    let contentArray = document.getElementsByClassName('content');
    for (let item of contentArray) {
        item.style.display = 'none';
    }

    let contentForm = document.getElementsByClassName('content-form');
    for (let item of contentForm) {
        item.style.display = 'block';
    }
    document.getElementById('modify-view').style.display = 'none';
    document.getElementById('modify-cancle').style.display = 'inline-block';
    document.getElementById('modify').style.display = 'inline-block';

    let response = await fetch(`/api/v1/apply/${applyId}`, {
        method: 'GET'
    });

    let data = await response.json();
    document.getElementById('name').value = data.name;
    document.getElementById('email').value = data.email;
    document.getElementById('phone1').value = data.phone.substring(0, 3);
    document.getElementById('phone2').value = data.phone.substring(3, 7);
    document.getElementById('phone3').value = data.phone.substring(7, 11);
    document.getElementById('voteTitle').value = data.voteTitle;
    document.getElementById('expectedCount').value = data.expectedCount;

    let start = new Date(data.startVote);
    document.getElementById('startYear').value = start.getFullYear();
    document.getElementById('startMonth').value = Number(start.getMonth()) + 1;
    document.getElementById('startDay').value = start.getDate();
    document.getElementById('startHour').value = Number(start.getHours());
    document.getElementById('startMinute').value = start.getMinutes();

    let end = new Date(data.endVote);
    document.getElementById('endYear').value = end.getFullYear();
    document.getElementById('endMonth').value = Number(end.getMonth()) + 1;
    document.getElementById('endDay').value = end.getDate();
    document.getElementById('endHour').value = Number(end.getHours());
    document.getElementById('endMinute').value = end.getMinutes();
}

/* 이용 신청서 삭제*/

async function removeApply(applyId) {
    
    if (!confirm("이용신청서를 삭제하시겠습니까?")) {
        return;
    }

    let response = await fetch(`/api/v1/apply/${applyId}`, {
        method: 'DELETE'
    });

    if (response && response.ok) {
        alert("삭제가 성공되었습니다");
    } else {
        alert("삭제 실패")
    }
}