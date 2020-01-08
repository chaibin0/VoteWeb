/*jshint esversion: 8 */

async function counting(applyId) {
    if (!confirm('개표하시겠습니까?')) {
        return;
    }

    let response = await fetch(`/api/v1/result/${applyId}`, {
        method: 'POST',
    });

    if (response && response.ok) {
        alert('개표 성공');
        window.location.href = `/apply/${applyId}/vote/view`;
    } else {
        alert("개표실패");
        console.log(response.text());
    }
}

/* 투표하기 */
async function sendVote(voteInfoId, uid) {

    let voteInfo = {
        voteInfoId: voteInfoId,
        voteList: []
    };
    let voteListDiv = document.getElementsByClassName('voteList');
    for (let voteDiv of voteListDiv) {
        let voteId = voteDiv.querySelector('#voteListId').value;
        let candidateItemsDiv = voteDiv.querySelectorAll('.candidate-items .candidate-item');

        let candidate = [];
        for (let candidateItemDiv of candidateItemsDiv) {
            let candidateId = candidateItemDiv.querySelector('#candidateId').value;
            let candidateSequenceNumber = candidateItemDiv.querySelector('#candidateSequenceNumber').value;

            let candidateItem = candidateItemDiv.querySelector(`input[name=voteList${voteId}]`);
            candidate.push({
                candidateSequenceNumber,
                candidateId,
                value: candidateItem.checked ? 1 : 0
            });
        }
        voteInfo.voteList.push({
            voteId,
            candidate
        });
    }

    let response = await fetch(`/api/v1/vote/${uid}/voting`, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(voteInfo)
    });


    if (response && response.ok) {
        alert('투표가 성공적으로 마쳤습니다');
        window.location.replace(`/vote/${uid}/voting/success`);
    } else {
        alert('전송실패');
        window.location.reload();
    }
}

async function signinVoting(uid) {
    let name = document.getElementById('name').value;
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;
    let phone = phone1 + phone2 + phone3;
    let url = '/api/v1/vote/sign/' + uid;
    let response = await fetch(url, {
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
        window.location.replace(`/vote/${uid}/voting`);
    } else {
        let error = document.getElementById('error');
        let errorMessage = document.getElementById('error-message');
        errorMessage.innerText(await response.text());
        error.style.display = 'block';
    }
}