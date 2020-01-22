/*jshint esversion: 8 */

function approvalApplication(id) {

    fetch('/api/v1/admin/apply/approval', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id
        })
    }).then((response) => {
        alert('승인 성공하였습니다');
        window.location.reload();
    }).catch((error) => {
        alert('에러');
    });
}

function rejectApplication(id) {

    fetch('/api/v1/admin/apply/reject', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id
        })
    }).then((response) => {
        alert('승인 거부하였습니다');
        window.location.reload();
    }).catch((error) => {
        alert('에러');
    });

}


async function registerBlackUser(id) {
    let response = await fetch('/api/v1/admin/user/black', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id,
            black: true
        })
    });

    if (response && response.ok) {
        alert("성공");
        location.reload();
    } else {
        alert("실패");
    }
}

async function cancleBlackUser(id) {
    let response = await fetch('/api/v1/admin/user/black', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id,
            black: false
        })
    });

    if (response && response.ok) {
        alert("성공");
        location.reload();
    } else {
        alert("실패");
    }
}

function searchUserinAll() {

    let search = document.getElementById('search').value;
    location.href = `/admin/user?search=${search}`;
}

function searchUserBlack() {
    let search = document.getElementById('search').value;
    location.href = `/admin/user/black?search=${search}`;
}

async function removeUser(id) {
    let response = await fetch(`/api/v1/admin/user?id=${id}`, {
        method: 'DELETE'
    });

    if (response && response.ok) {
        alert('삭제되었습니다');
        location.reload();
    } else {
        alert('삭제실패');
    }
}