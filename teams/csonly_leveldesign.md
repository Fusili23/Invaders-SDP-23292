# Team 'C# Only' 
## Team Introduction
Hello, we are team 'C# Only'. Our team consists of the following members:

Member

| Name                                             | Email                   | Github Address                     |
|--------------------------------------------------|-------------------------|------------------------------------|
| Kisan Nam (Leader) | soumt@hanyang.ac.kr     | https://github.com/soumt-r         |
| E.Khongor                                    | Hongorhongor3@gmail.com | https://github.com/spicytortillabn |
| LEE SANGHYEON                                | hyeoni000401@gmail.com  | https://github.com/rudwnl          |
| Lee Seong min   | aydd4488@gmail.com      | https://github.com/lookback03      |
| CHO GUNHA                                    | jojo88092@gmail.com     | https://github.com/GUNHA96         |
| Lee SeokMin                                  | seokmin04@hanyang.ac.kr | https://github.com/seokmin04       |
| Amartsogt Tsogtbaatar                        | amartsogttsogtbaatar@gmail.com | https://github.com/Amraa-gif       |

### **Team Requirements**
- 우리 팀은 level design에 집중하여 게임의 난이도와 재미를 향상시키는 것을 목표로 합니다.
- space invaders 게임의 레벨 디자인을 개선하여 플레이어에게 더 흥미로운 경험을 제공하고자 합니다.

### **Detailed Requirements**

1.  레벨에 따라 적의 **이동 속도, 투사체 속도, 공격 빈도를 점진적으로 상향**시킵니다.
2.  레벨이 진행될수록 **멀티샷, 유도탄 등 새로운 아이템이 등장**하도록 아이템 테이블을 설계합니다. (`Item System` 팀과 협업 필요)
3.  **특정 스테이지 클리어 시 고유한 공격 패턴을 가진 보스가 등장**하는 보스전을 기획합니다.
4.  스테이지별 **적 외형(색상) 및 배경 음악(BGM)을 다르게 설정**합니다. (`Visual Effect`, `Sound Effects/BGM` 팀과 협업 필요)
5.  **레벨 클리어 시 달성되는 업적의 트리거(조건)를 정의**합니다. (`Records & Achievements` 팀과 협업 필요)
6.  적의 공격이 벽에 튕기는 특수 기믹이 있는 스테이지를 설계합니다. (`Player & Enemy Ship Variety` 팀의 플레이어 상하 이동 기능 구현 여부 확인 필요) **(Optional)**
7.  **특정 조건으로 진입하는 히든 스테이지(이스터에그)**를 구현합니다. **(Optional)**

---

### **Dependencies on Other Teams**

1.  **Item System:** 레벨 디자인에 맞춰 등장할 **신규 아이템(유도탄, 멀티샷, 생명+1 등)의 실제 기능 구현**이 필요합니다.
2.  **Records & Achievements System:** 특정 레벨 클리어 시 **업적이 표시되고 기록되는 시스템**의 구현이 필요합니다.
3.  **Visual/Sound Effects Teams:** 상위 레벨에 사용될 **더 화려한 시각 효과(이펙트)와 다양한 BGM, 효과음 제작**이 필요합니다.
