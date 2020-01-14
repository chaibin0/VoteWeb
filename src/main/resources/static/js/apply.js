/*jshint esversion: 8 */
/* 이용 신청서 */

function checkApplyForm() {
    let phoneReg = /\d\d\d\d/;

    let name = document.getElementById('name').value.trim();
    let email = document.getElementById('email').value.trim();
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;

    if (!name) {
        alert("이름을 입력하세요");
        return false;
    }

    
    // if (phone1.value != '010') {
    //     alert('휴대전화번호를 잘못 입력하셨습니다.');
    //     return false;
    // }

    // if (!phoneReg.exec(phone2) || !phoneReg.exec(phone3)) {
    //     alert("휴대전화번호를 잘못 입력하셨습니다.");
    //     return false;
    // }

    let voteTitle = document.getElementById('voteTitle').value.trim();

    if (!voteTitle) {
        alert("제목을 입력하세요");
        return false;
    }
    return true;
}

/* 이용 신청서 등록 */
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
    fetch('/api/v1/user/info')
        .then((response) => response.json())
        .then(data => {
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

async function modifyView(applyId) {
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
    console.log(data);
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

function cancleModify() {
    let contentArray = document.getElementsByClassName('content');

    for (let item of contentArray) {
        item.style.display = 'block';
    }

    let contentForm = document.getElementsByClassName('content-form');
    for (let item of contentForm) {
        item.style.display = 'none';
    }
    document.getElementById('modify-view').style.display = 'inline-block';
    document.getElementById('modify-cancle').style.display = 'none';
    document.getElementById('modify').style.display = 'none';
}

async function modifyApply(id) {

    if(!confirm('수정할 시 관리자로부터 재 인증이 필요합니다. 수정하시겠습니까?')){
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
        id,
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
        window.location.reload();
    } else {
        console.log(await response.text());
        alert('에러@@');
        window.location.reload();
    }
}

async function removeApply(id) {
    if (!confirm("이용신청서를 삭제하시겠습니까?")) {
        return;
    }

    let response = await fetch(`/api/v1/apply/${id}`, {
        method: 'DELETE'
    });

    if (response && response.ok) {
        alert("삭제가 성공되었습니다");
        window.location.replace('/apply');
    } else {
        console.log(error);
        alert('에러@@');
    }
}

async function counting(id) {
    if (!confirm('개표하시겠습니까?')) {
        return;
    }

    let response = await fetch(`/api/v1/result/${id}`, {
        method: 'POST',
    });

    if (response && response.ok) {
        alert("개표 끝");
        window.location.reload();
    } else {
        alert(await response.text())
    }
}