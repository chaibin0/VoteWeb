let checkId = false;

function changeId() {
    let div = document.getElementById('checkResult');
    checkId = false;
    div.innerText = '';

}

function checkSameId() {
    let userId = document.getElementById('userId').value;
    let div = document.getElementById('checkResult');
    fetch(`/api/v1/user/check-id?userId=${userId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => response.json()).then((response) => {
        console.log(response);
        if (!response.checkUserId) {
            checkId = true;
            div.innerText = '아이디 사용가능';
        } else {
            checkId = false;
            div.innerText = '중복된 아이디';
        }
    }).catch((error) => {
        console.log(error);
    })


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

    let response = await fetch('/api/v1/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id,
            name,
            password,
            phone
        })
    });

    if (response && response.ok) {
        alert('회원가입이 성공하였습니다.');
        window.location.replace('/');
    } else {
        alert(await response.text());
    }
}