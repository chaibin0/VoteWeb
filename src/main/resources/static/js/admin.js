/*jshint esversion: 8 */

function approvalApplication(id) {

    fetch('/api/v1/admin/apply/approval', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id
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
            id: id
        })
    }).then((response) => {
        alert('승인 거부하였습니다');
        window.location.reload();
    }).catch((error) => {
        alert('에러');
    });
}