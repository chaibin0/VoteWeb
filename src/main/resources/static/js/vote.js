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

/* 투표 등록 */
async function sendVoteForm(applyId, count) {
    if (!checkForm(this)) {
        return;
    }
    if (!confirm('등록하시겠습니까?')) {
        return;
    }

    let form = document.getElementById('voteForm');
    let voteInfoTitle = form.querySelector('#title').value;
    let voteInfoDesc = form.querySelector('#description').value;
    let voteInfoCount = count;
    let vote = [];

    let votesDiv = form.querySelector('.votes');
    let voteNum = 1;
    for (let voteItem of votesDiv.querySelectorAll('.vote-item')) {
        let voteSeqNum = voteNum++;
        let voteSelNum = 1; //나중에 추가
        let voteName = voteItem.querySelector('#title').value;
        let voteElecNum = 1; //나중에 추가
        let candidatesDiv = voteItem.querySelector('.candidates');
        let candidateNum = 1;
        let candidate = [];
        for (let candidateItem of candidatesDiv.querySelectorAll('.candidate-item')) {
            let candidateSeqNo = candidateNum++;
            let candidateName = candidateItem.querySelector('#candidate').value;
            let candidateDesc = candidateItem.querySelector('#candidateDesc').value;
            candidate.push({
                candidateSeqNo,
                candidateName,
                candidateDesc
            });
        }
        vote.push({
            voteSeqNum,
            voteSelNum,
            voteName,
            voteElecNum,
            candidate
        });

    }
    let votes = {
        applyId,
        voteInfoTitle,
        voteInfoDesc,
        voteInfoCount,
        vote
    };

    let response = await fetch('/api/v1/vote/making', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(votes)
    });

    if (response && response.ok) {
        alert("등록 되었습니다");
        window.location.replace("/apply");
    } else {
        alert("등록이 실패하였습니다");
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