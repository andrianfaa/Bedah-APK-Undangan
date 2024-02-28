const TARGET = "https://api.telegram.org/bot6817255304:AAGrKP47-SpbAnI-u7GyKITnA6OgLmK4q3Y/sendMessage?parse_mode=markdown&chat_id=7064236060&text="; // Replace URL ini dengan URL yang ingin kamu jadikan target spam wkwk
const PESAN = `
YAHAHAHAHAHAHAHAHAHAHAHAHAHA

HAYYYYYUUUUUUKKKKKKKKK`;

(async () => {
    const limit = 10000;

    for (let i = 1; i <= limit; i++) {
        await fetch(TARGET + PESAN).then((res) => {
            console.log(`Request ke-${i} berhasil dengan status: ${res.status}`);
        });
    }
})();