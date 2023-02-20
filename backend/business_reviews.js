const express = require('express')
const axios = require('axios');
const { response } = require('express');
const router = express.Router()
const base_url = require('./server.js').base_url;
const HEADERS = require('./server.js').headers;
const cors = require('cors');


// Reviews Route
router.get('/', cors(), (req, res) => {    
    // Payload
    let config = {
        headers:HEADERS
    }
    let b_id = String(req.query.b_id);

    // axios.get(base_url+`businesses/7XhSOz47twW5yGLTgTaREw/reviews`, config)
    axios.get(base_url+`businesses/`+b_id+`/reviews`, config)
        .then(function (response) {
            let review_list=[]
            if('reviews' in response.data && Object.keys(response.data.reviews).length > 0) {
                // If reviews are <= 3
                if (Object.keys(response.data.reviews).length <= 3){
                    review_list=[]; // Empty list first
                    response.data.reviews.forEach(element => {
                        review_list.push(
                            {
                                'id':element.id,
                                'rating':`${element.rating}/5`,
                                'name':element.user.name,
                                'time_created':String(element.time_created).substring(0,10),
                                'text':element.text
                            }
                        );
                    });
                }
                else {
                    review_list=[]; // Empty List first
                    for (var i = 0; i < 3; i++) {
                        review_list.push(
                            {
                                'id':response.data.reviews[i].id,
                                'rating':`${response.data.reviews[i].rating}/5`,
                                'name':response.data.reviews[i].user.name,
                                'time_created':String(response.data.reviews[i].time_created).substring(0,10),
                                'text':response.data.reviews[i].text
                            });
                    }
                }
            }

            console.log(response.status);
            res.json({'status' : response.status, 'response' : review_list});
        })
        .catch(function (error) {
            console.log(error);
            res.json({'status':error.response.status, 'response' : []});
        })
})

module.exports = router