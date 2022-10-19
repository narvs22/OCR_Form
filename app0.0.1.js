//imports
const express = require("express");
const app = express();
const fs = require("fs"); //read/write files
const multer = require("multer"); //upload file to the server
const { createWorker } = require ("tesseract.js"); //reads

const worker = createWorker({ //analyses the image
    logger: m=> console.log(m)//new format to perform analysis of image from tesseract from tesseract.js site

});
//storage:
const storage = multer.diskStorage(
    {
    destination: (req,file,cb) => 
    {
        cb(null,"./uploads");
    },
    filename: (req,file,cb) =>
    {
        cb(null,file.originalname);
    }
});

const upload = multer({storage: storage}).single("OCR");

app.set("view engine", "ejs");

//Routing

app.get("/", (req, res) => 
{
    res.render("index");
});
app.post("/uploads", (req, res) => {
    upload(req,res, err => {
        fs.readFile(`./uploads/${req.file.originalname}`, (err,data)=>
        {
            if(err) return console.log("this is your error",err);
            async function getTextFromImage() {
                await worker.load()
                await worker.loadLanguage("eng")
                await worker.initialize("eng")
                const { data: { text } } = await worker.recognize(data);  
                res.send(text);
                console.log(text);
                await worker.getPDF("Tesseract OCR Result");
                fs.writeFileSync("tesseract-ocr-result.pdf", Buffer.from(text));
                await worker.terminate();
            }
            getTextFromImage();
            //res.redirect('/download');
       });
    });
});
app.get("/download", function(req,res){
   const file=`${__dirname}/tesseract-ocr-result.pdf`;
    res.download(file);
});
//Start server

const PORT = 5000 || process.env.PORT;
app.listen(PORT, () => console.log(`Hey Im running on PORT ${PORT}`));