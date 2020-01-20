/*jshint esversion: 8 */

let checkId = false;

function changeId() {
    let div = document.getElementById('checkResult');
    checkId = false;
    div.innerText = '';
}

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
        if (!(await response.json()).checkUserId) {
            checkId = true;
            div.innerText = '아이디 사용가능';
            return;
        } else {
            checkId = false;
            div.innerText = '중복된 아이디';
            return;
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
        return;
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

async function modifyUser() {

    let name = document.getElementById('name').value;
    let phone = document.getElementById('phone').value;
    let email = document.getElementById('email').value;

    let response = await fetch('/api/v1/user/info', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name,
            email,
            phone
        })
    });

    if (response && response.ok) {
        alert('수정완료');
        window.location.replace('/mypage');
    } else {
        alert((await response.json()).message);
    }
}

async function modifyPassword() {
    let password = document.getElementById('password').value;
    let newPassword = document.getElementById('newPassword').value;
    let newPasswordCheck = document.getElementById('newPasswordCheck').value;

    if (newPassword !== newPasswordCheck) {
        alert('비밀번호가 동일하지 않습니다.');
        return;
    }

    let response = await fetch('/api/v1/user/password', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            password,
            newPassword,
            newPasswordCheck
        })
    });

    if (response && response.ok) {
        alert('수정완료');
        window.location.replace('/mypage');
    } else {
        alert((await response.json()).message);
    }

}

async function removeUser() {
    if (!confirm("삭제하시겠습니까?")) {
        return;
    }
    if (!confirm("진짜 삭제하시겠습니까?")) {
        return;
    }

    let response = await fetch('/api/v1/user', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (response && response.ok) {
        alert('잘가요');
        location.href = "/user/logout";
    }
}