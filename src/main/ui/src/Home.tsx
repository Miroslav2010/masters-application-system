import React from "react";
import {PageLayout} from "./PageLayout";
import {Card, CardContent, Typography} from "@mui/material";

const headers = [
    '1. Пријава',
    '2. Проверка од ментор',
    '3. Проверка од студенска служба',
    '4. Проверка од ННК',
    '5. Секретар валидира',
    '6. Студент прикачува драфт',
    '7. Менторот валидира',
    '8. Секретар валидира',
    '9. Проверка од ННК',
    '10. Секретар валидира',
    '11. Проверка на драфт',
    '12. Валидирање на извештај',
    '13. Секретар валидира',
    '14. Студентска служба валидира',
    '15. Процесот е завршен'
];
const validates = [
    'Студент',
    'Ментор',
    'Студентска служба',
    'Раководител на ННК',
    'Секретар',
    'Студент',
    'Ментор',
    'Секретар',
    "Раководител на ННК",
    "Секретар",
    "Ментор, Член на комисија, Студент",
    "Член на комисија",
    "Секретар",
    "Студентска служба",
    ""
];
const content = [
    'Студентот пополнува пријава и прикачува документи.',
    'Менторот валидира',
    'Студентската служба проверува и валидира',
    'Наставно научна комисија валидира',
    'Секретарот валидира',
    'Студентот подготвува и прикачува драфт документ и се праќа известување до професор',
    'Менторот избира членови на комисија и валидира',
    'Секретарот валидира',
    'Проверка од ННК',
    'Секретарот валидира',
    'Членови на комисија внесуваат забелешки, студентот го менува драфтот, менторот валидира',
    'Членови на комисија проверуваат извештај и валидираат',
    'Секретар додава архивски број и валидира',
    'Студентска служба архивира и валидира',
    'Процесот е завршен и се чека одбрана на трудот'
];



export const Home = () => {
    return (
      <PageLayout>
          {
              headers.map((value, index) =>
                  <Card key={index} style={{marginTop: "2rem", marginBottom: "2rem"}}>
                      <CardContent>
                          <Typography gutterBottom variant="h5" component="div">
                              {value}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                              <strong>валидира:</strong> {validates[index]}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                              <strong>опис:</strong> {content[index]}
                          </Typography>
                      </CardContent>
                  </Card>
              )
          }
      </PageLayout>
    );
}