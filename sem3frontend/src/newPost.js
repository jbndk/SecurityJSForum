import mainURL from "./settings";
import facade from "./apiFacade";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useState } from "react";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';

const NewPost = () => {

    const [errorMessage, setErrorMessage] = useState(false);
    const [savedStatus, setSavedStatus] = useState(false);

    let cat = "";
    let img = "";
    let cont = "";
    const init = { cat, img, cont };

    const [post, setPost] = useState(init);


    const categoryOptions = [
        'general', 'category1', 'category2', 'category3'
    ];

    const defaultCategoryOptions = categoryOptions[0];

    const addPost = () => {

        const userName = localStorage.getItem('userName');

        const newPostOptions = facade.makeOptions("POST", false, {
            category: cat,
            imageurl: img,
            content: cont,
            author: userName
        });

        fetch(mainURL + "/api/post/new", newPostOptions)
            .then(res => res.json())
            .then(res => {
                if (res === 'Post successfully added') {
                    setSavedStatus(true);
                    setErrorMessage(false);
                } else {
                    setSavedStatus(false);
                    setErrorMessage(true);
                }
            });
    }

    const onChange = (evt) => {
        setPost({
            ...post, [evt.target.id]: evt.target.value,
        });
    };


    return (
        <div class="sm col-8">
            <br />
            <h2>Add a new post:</h2>
            <br />
            <div className="col-sm-4">
                <form onChange={onChange} >
                    <Dropdown options={categoryOptions} value={defaultCategoryOptions} placeholder="Enter category here" id="category" />
                    <br />
                    <input className="form-control" placeholder="Image URL" id="imageurl" />
                    <br />
                    <textarea className="span12" rows="10" placeholder="Write your post here" id="content" />
                    <br />
                    <button className="btn btn-primary" onClick={addPost}>Submit post</button>
                </form>
            
            </div>
            <br /><br />


            {errorMessage && (
                <div className="col-sm-4">
                </div>
            )}
            <br /><br /><br />
        </div>

    )
}

export default NewPost;