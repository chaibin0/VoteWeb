/*jshint esversion: 8 */

/* 유권자 정보 확인 */
async function signVoterInfo() {

    let name = document.getElementById('name').value;
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;
    let phone = phone1 + phone2 + phone3;
    let response = await fetch('/api/v1/user/apply/validation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name,
            phone
        })
    });

    if (response && response.ok) {
        window.location.replace("/apply/list");
    } else {
        console.log((await response.json()).message);
    }
}


let checkId = false;

async function checkSameId() {
    let userId = document.getElementById('userId').value;
    let div = document.getElementById('checkResult');
    let response = await fetch(`/api/v1/user/check-id?userId=${userId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (response && response.ok) {
        if (!response.checkUserId) {
            checkId = true;
            div.innerText = '아이디 사용가능';
        } else {
            checkId = false;
            div.innerText = '중복된 아이디';
        }
    } else {
        console.log((await response.json()).message);
    }
}

async function signup() {
    if (!checkId) {
        alert("아이디 중복확인하세요");
        return;
    }

    let id = document.getElementById('userId').value;
    let password = document.getElementById('password').value;
    let passwordChk = document.getElementById('passwordChk').value;
    if (password !== passwordChk) {
        alert("비밀번호 확인 제대로 하세요");
    }

    let name = document.getElementById('name').value;
    let phone = document.getElementById('phone').value;
    let email = document.getElementById('email').value;
    let response = await fetch('/api/v1/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id,
            name,
            password,
            email,
            phone
        })
    });

    if (response && response.ok) {
        alert('회원가입이 성공하였습니다.');
        window.location.replace('/');
    } else {
        alert((await response.json()).message);
    }
}