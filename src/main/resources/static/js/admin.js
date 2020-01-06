/*jshint esversion: 8 */

async function approvalApplication(id) {

    let response = fetch('/api/v1/apply/approval', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id
        })
    });

    if (response && response.ok) {
        alert('승인 성공하였습니다');
        window.location.reload();
    } else {
        alert('에러');
    }
}

async function rejectApplication(id) {

    let response = fetch('/api/v1/apply/reject', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id
        })
    });
    
    if(response && response.ok){
        alert('승인 거부하였습니다');
        window.location.reload();
    }else{
        alert('에러');
    }
}