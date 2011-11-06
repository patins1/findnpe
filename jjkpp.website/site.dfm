object PageContainer1: TPageContainer1
  Left = -4
  Top = -23
  Caption = 'site.dfm'
  ClientHeight = 775
  ClientWidth = 1920
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clBlack
  Font.Height = -16
  Font.Name = 'Arial'
  Font.Style = []
  KeyPreview = True
  OldCreateOrder = False
  Position = poDesigned
  Scaled = False
  Visible = True
  WindowState = wsMaximized
  PixelsPerInch = 96
  TextHeight = 18
  object index: TdhPage
    Left = 0
    Top = 0
    ImageType = bitTile
    Style.BackgroundImage.Data = {
      0954506E67496D61676589504E470D0A1A0A0000000D49484452000004F00000
      00010802000000DF20C7FB000000BF4944415478DAE596410E84300C03E3AFF2
      B27DD9BE650F70A034A10EB891AAE500951532958549B17DBE76BAD03C3AFDAA
      C2D7FB7ADCF64970B1DF0AB9CDB284EBA81417814E7111558FD108F4BEDEDB2E
      FC521D3778A18A8BB0CF2C2E067DF4DC4C2A5F7DA5FEB28A7B3542C9F56DE555
      1517617D9E6BEBA692FC8127B85C2A955C58E3B3824B0EE83CD79EA4C337E28F
      524946E321F7F5804E7345A9248FAFF25452DC9552B9CAB1D987CC4DC7214F1F
      58A64E659A5B3BA0EDC6D431F707218902FF9426073F0000000049454E44AE42
      6082}
    Style.BackgroundRepeat = cbrRepeatY
    Style.BackgroundColor = 14590050
    Style.FontFamily = 'Arial'
    AutoSizeXY = asNone
    Right = 0
    Bottom = 0
    UseIFrame = False
    Title = 'FindNPE - manage null pointer contracts'
    FTPURL = 'ftp://p8288468:5yMTPDtZ@www.kiegeland.com/findnpe/'
    HTTPURL = 'http://findnpe.kiegeland.com'
    GeneratedCSSFile = 'dfm2html.css'
    object Panel1: TdhPanel
      Left = 0
      Top = 0
      Width = 1232
      Height = 764
      Style.Margin = '16'
      Style.BackgroundColor = 33554431
      AutoSizeXY = asNone
      object Label2: TdhLabel
        Left = 32
        Top = 32
        Width = 696
        Height = 41
        Text = 
          'FindNPE <small>manage null pointer contracts <i><i><Label11><sup' +
          '> for Java/Eclipse programmers</sup></Label11></i></i></small>'
        Style.FontSize = '36'
        Style.FontFamily = 'arial'
        Style.Effects.AntiAliasing = True
        AutoSizeXY = asXY
      end
      object Link1: TdhLink
        Left = 32
        Top = 280
        Width = 33
        Height = 18
        Text = 'FAQ'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = faq
      end
      object Link2: TdhLink
        Left = 32
        Top = 240
        Width = 72
        Height = 18
        Text = 'Installation'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = install
      end
      object Link3: TdhLink
        Left = 32
        Top = 120
        Width = 79
        Height = 18
        Text = 'Introduction'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = home
      end
      object Link6: TdhLink
        Left = 32
        Top = 440
        Width = 40
        Height = 18
        Text = 'Order'
        Style.Visibility = cviHidden
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
      end
      object Panel2: TdhPanel
        Left = 160
        Top = 104
        Height = 628
        Style.Border.Width = 1
        Style.Border.Color = 14590050
        Style.Border.Style = cbsSolid
        Style.BackgroundColor = 33554431
        AutoSizeXY = asNone
        Anchors = [akLeft, akTop, akRight]
        Right = 32
        object PageControl1: TdhPageControl
          Left = 440
          Top = 8
          Width = 24
          Height = 24
          ActivePage = home
          FixedHeight = False
          object home: TdhPage
            Left = 8
            Top = 16
            Height = 600
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 80
            UseIFrame = False
            object Label19: TdhLabel
              Left = 8
              Top = 16
              Width = 920
              Height = 108
              Text = 
                'FindNPE offers a solution for controlling null pointers in Java ' +
                'code. Thus, the risk of NPEs (NullPointerExceptions)'#10'being throw' +
                'n on customer'#39's side can be minimized. FindNPE offers annotation' +
                's for a fine-grained control of which variables are allowed to b' +
                'e null at runtime.'#10'Since FindNPE uses static analysis of the Jav' +
                'a program at compile time, a program'#39's execution behavior is not' +
                ' affected. '#10'<br><br>FindNPEs basic rule to reduce NPEs is: If th' +
                'ere is a possibility that an object can be null, then compilatio' +
                'n errors are produced at places where it is accessed.  '#10'This is ' +
                'demonstrated in the following example:'
              AutoSizeXY = asY
            end
            object Link14: TdhLink
              Left = 8
              Top = 152
              Width = 505
              Height = 51
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\rule1.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 505
              Style.BackgroundImage.Height = 51
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
          end
          object usage: TdhPage
            Left = 8
            Top = 16
            Height = 1744
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label10: TdhLabel
              Left = 8
              Top = 16
              Width = 920
              Height = 54
              Text = 
                'After installing FindNPE, errors like the example on the Inroduc' +
                'tion page are shown. However such NPE hazards are generally quit' +
                'e few. NPEs more likely happen by&nbsp;having methods which are ' +
                'assumed to return a non-null value but in fact can return null. ' +
                'Without preparation, also FindNPE cannot detect such inter-metho' +
                'd NPE hazards: '
              AutoSizeXY = asY
            end
            object Image1: TdhLink
              Left = 16
              Top = 88
              Width = 484
              Height = 190
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic1.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 484
              Style.BackgroundImage.Height = 190
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label12: TdhLabel
              Left = 8
              Top = 320
              Width = 952
              Height = 36
              Text = 
                'To make visible the obviously not reported problem, the first st' +
                'ep is to annotate the class with @NonNullByDefault (which is equ' +
                'ivalent to annotating each method /parameter / field of the clas' +
                's with @NonNull):'
              AutoSizeXY = asY
            end
            object Link8: TdhLink
              Left = 88
              Top = 1040
              Width = 487
              Height = 188
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic2.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 487
              Style.BackgroundImage.Height = 188
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label13: TdhLabel
              Left = 16
              Top = 640
              Width = 952
              Height = 54
              Text = 
                '@NonNull for a method means, that all return statements are chec' +
                'ked to return a non-null value, so we have a violation of this c' +
                'ontract in methodA. Similar, it would mean for parameters and fi' +
                'elds, that they can only be assigned a non-null value. Now, to s' +
                'olve the reported problem, one can annotate the method explicite' +
                'ly with @CanBeNull by just applying the proposed quick fix:'
              AutoSizeXY = asY
            end
            object Link9: TdhLink
              Left = 16
              Top = 1336
              Width = 505
              Height = 205
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic3.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 505
              Style.BackgroundImage.Height = 205
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label15: TdhLabel
              Left = 8
              Top = 1584
              Width = 952
              Height = 90
              Text = 
                'The reason why such errors can not be fixed automatically, is ma' +
                'inly because the error could also have been fixed differently by' +
                ' coding that methodA not returns null but e.g.&nbsp;an empty str' +
                'ing. So it has to be decided whether the calling method or the c' +
                'alled method has to be fixed. This choice depends on what the pr' +
                'ogrammer "had in mind"  whether the method is allowed to return ' +
                'null or not allowed to do so. Of course it can also be that he n' +
                'ever thought about it and  is forced to do so right now. The NPE' +
                ' annotations can be used to explicitely express   the desired co' +
                'ntract.'
              AutoSizeXY = asY
            end
            object Label16: TdhLabel
              Left = 8
              Top = 1288
              Width = 952
              Height = 18
              Text = 
                'Finally, the desired error is displayed, saying that &quot;s&quo' +
                't; can be null when it is accessed.  The programmer can fix this' +
                ' error like here:'
              AutoSizeXY = asY
            end
            object Link11: TdhLink
              Left = 16
              Top = 376
              Width = 487
              Height = 239
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic5.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 487
              Style.BackgroundImage.Height = 239
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Image2: TdhLink
              Left = 24
              Top = 712
              Width = 734
              Height = 299
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic6.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 734
              Style.BackgroundImage.Height = 299
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label30: TdhLabel
              Left = 168
              Top = 1144
              Width = 0
              Height = 32
              Text = 'Label30'
              AutoSizeXY = asY
            end
            object Label32: TdhLabel
              Left = 40
              Top = 1120
              Width = 100
              Height = 18
              Text = '=>'
              AutoSizeXY = asY
            end
          end
          object install: TdhPage
            Left = 8
            Top = 16
            Height = 512
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label6: TdhLabel
              Left = 16
              Top = 16
              Width = 191
              Height = 18
              Text = 'Update Site for Eclipse 3.6:'
              AutoSizeXY = asXY
            end
            object Link5: TdhLink
              Left = 216
              Top = 16
              Width = 272
              Height = 18
              Text = 'http://findnpe.kiegeland.com/3.6/update'
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
              Link = 'http://findnpe.kiegeland.com/3.6/update'
            end
            object Label9: TdhLabel
              Left = 16
              Top = 48
              Width = 392
              Height = 108
              Text = 
                'After installation, you should restart Eclipse and make sure, th' +
                'at Preferences -> JDT Weaving is ENABLED.'#10'<br/><br/>'#10'The  FindNP' +
                'E annotations must be downloaded separately and linked as a norm' +
                'al JAR to the Java project, see <Link15>Q2</Link15>.'
              AutoSizeXY = asY
            end
            object StyleSheet4: TdhStyleSheet
              Left = 632
              Top = 112
              Width = 28
              Height = 28
              Expanded = False
              ExpandedWidth = 100
              ExpandedHeight = 100
              Expanded = False
              object Link15: TdhLink
                Left = 0
                Top = 0
                Height = 18
                Text = 'Q2'
                AutoSizeXY = asXY
                Align = alTop
                Right = 0
                PreferDownStyles = True
                Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                LinkPage = faq
                LinkAnchor = Q2
              end
            end
          end
          object faq: TdhPage
            Left = 8
            Top = 16
            Height = 1544
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Q1: TdhLabel
              Left = 24
              Top = 48
              Width = 1000
              Height = 146
              Text = 
                '<big><big>Q1</big></big><br/> '#10'<br/><b>I get new error messages ' +
                'which was not there before:'#10'<br/>'#10'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                '&nbsp;&nbsp;&nbsp;&nbsp;<Label5><code>The type %x% cannot be res' +
                'olved. It is indirectly referenced from required .class files</c' +
                'ode></Label5>'#10'<br/></b>'#10'<br/>'#10'FindNPE requires to evaluate annot' +
                'ations from super classes of a referenced class. For this reason' +
                ', the superclass must be in the build path of the project. To ac' +
                'hieve this, you have to add a dependency to the project or plugi' +
                'n which contains this class file.'
              AutoSizeXY = asY
            end
            object Q2: TdhLabel
              Left = 24
              Top = 224
              Width = 1000
              Height = 164
              Text = 
                '<big><big>Q2</big></big><br/> '#10'<br/><b>I get the error message :' +
                #10'<br/>'#10'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<La' +
                'bel5><code>The import findnpe cannot be resolved</code></Label5>' +
                #10'<br/></b>'#10'<br/>'#10'You try to import one of the three annotations ' +
                'CanBeNull, NonNull or NonNullByDefault, but they are unknown to ' +
                'the Java compiler. As normal Java classes, annotations must be o' +
                'n the build path. '#10'For this reason, download <Link7>findnpe.anno' +
                'tations.jar</Link7>, copy it to your Java project e.g. in its ro' +
                'ot folder and choose "Add to build path" from the context menu. '
              AutoSizeXY = asY
            end
          end
          object features: TdhPage
            Left = 8
            Top = 16
            Height = 688
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label7: TdhLabel
              Left = 16
              Top = 24
              Width = 496
              Height = 19
              Text = 'Kinds of reported problems:'
              Style.FontWeight = cfwBold
              AutoSizeXY = asY
            end
            object Label20: TdhLabel
              Left = 72
              Top = 112
              Width = 480
              Height = 36
              Text = 
                'Means that the source code expression in question returns an obj' +
                'ect which can be null, but this object is subsequently accessed.'
              AutoSizeXY = asY
            end
            object Label21: TdhLabel
              Left = 72
              Top = 328
              Width = 496
              Height = 18
              Text = 
                'Like the previous one, but with the hint that the expression is ' +
                'always null'
              AutoSizeXY = asY
            end
            object Label23: TdhLabel
              Left = 8
              Top = 80
              Width = 99
              Height = 18
              Text = '"NPE Hazard"'
              Use = rule
              AutoSizeXY = asXY
            end
            object Label24: TdhLabel
              Left = 8
              Top = 296
              Width = 337
              Height = 18
              Text = '"Expected NonNull value, but value is always null"'
              Use = rule
              AutoSizeXY = asXY
            end
            object Label22: TdhLabel
              Left = 72
              Top = 200
              Width = 480
              Height = 72
              Text = 
                'Means that the source code expression in question returns an obj' +
                'ect which can be null, but it is returned by a method / passed t' +
                'o a parameter / assigned to a field / variable, which is declare' +
                'd as @NonNull.'
              AutoSizeXY = asY
            end
            object Label31: TdhLabel
              Left = 8
              Top = 168
              Width = 175
              Height = 18
              Text = '"Expected NonNull value"'
              Use = rule
              AutoSizeXY = asXY
            end
          end
          object contact: TdhPage
            Left = 8
            Top = 16
            Height = 600
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 24
            UseIFrame = False
            object PageControl2: TdhPageControl
              Left = 63
              Top = 69
              Width = 24
              Height = 24
              ActivePage = Page7
              FixedHeight = True
              object Page6: TdhPage
                Left = 15
                Top = 13
                Width = 216
                Height = 256
                AutoSizeXY = asNone
                UseIFrame = False
                object ContactForm: TdhHTMLForm
                  Left = 8
                  Top = 8
                  Width = 200
                  Height = 240
                  AutoSizeXY = asNone
                  Method = fmtPost
                  object Submit1: TdhLink
                    Left = 8
                    Top = 208
                    Width = 63
                    Height = 24
                    Text = 'Submit'
                    AutoSizeXY = asXY
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    LinkPage = Page7
                    FormButtonType = fbSubmit
                  end
                  object Label25: TdhLabel
                    Left = 4
                    Top = 6
                    Width = 81
                    Height = 18
                    Text = 'Your Name:'
                    AutoSizeXY = asXY
                  end
                  object name: TdhEdit
                    Left = 8
                    Top = 24
                    Width = 184
                    Height = 23
                    AutoSizeXY = asY
                  end
                  object Label26: TdhLabel
                    Left = 8
                    Top = 46
                    Width = 49
                    Height = 18
                    Text = 'E-Mail:'
                    AutoSizeXY = asXY
                  end
                  object email: TdhEdit
                    Left = 8
                    Top = 64
                    Width = 184
                    Height = 23
                    AutoSizeXY = asY
                  end
                  object Label27: TdhLabel
                    Left = 8
                    Top = 86
                    Width = 69
                    Height = 18
                    Text = 'Message:'
                    AutoSizeXY = asXY
                  end
                  object message: TdhMemo
                    Left = 8
                    Top = 104
                    Width = 184
                    Height = 96
                    AutoSizeXY = asNone
                  end
                  object Reset1: TdhLink
                    Left = 80
                    Top = 208
                    Width = 58
                    Height = 24
                    Text = 'Reset'
                    AutoSizeXY = asXY
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    FormButtonType = fbReset
                  end
                  object access: TdhHiddenField
                    Left = 144
                    Top = 208
                    Width = 24
                    Height = 24
                    Value = 'dfm2html'
                  end
                end
              end
              object Page7: TdhPage
                Left = 15
                Top = 13
                Width = 216
                Height = 256
                Style.PaddingTop = 30
                AutoSizeXY = asNone
                UseIFrame = False
                object ProcessContactForm: TdhDirectHTML
                  Left = 0
                  Top = 30
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = 
                    '<?php'#13#10#9'$recipient = "joerg@kiegeland.com";'#13#10#9'$subject = "Contac' +
                    't form";'#13#10#13#10#9'if ($_POST["access"]!="dfm2html")  $err="Page incor' +
                    'rectly accessed (e.g. by a bot)"; else'#13#10#9'if ($_POST["name"] == "' +
                    '")  $err="You must specify a name!"; else'#13#10#9'if ($_POST["message"' +
                    '] == "")  $err="You must specify a message!"; else'#13#10#9'if ($recipi' +
                    'ent == "your@email.com") $err="No recipient defined by the web d' +
                    'esigner!"; else'#13#10#9'$err="";'#13#10#9'if ($err=="") {'#13#10#9#9'$msg= "";'#13#10#9#9'for' +
                    'each($_POST as $key=> $val) {'#13#10#9#9#9'if ($key != "access") $msg .= ' +
                    '$key." : ".$val."\n\n";'#13#10#9#9'}'#13#10#9#9'$header='#39#39';'#13#10#9#9'if (isset($_POST[' +
                    '"email"])) $header .= '#39'From:'#39'.$_POST['#39'email'#39']."\n";'#13#10#13#10#9#9'if (cou' +
                    'nt($_FILES)>0) {'#13#10#13#10#9#9'$boundary = strtoupper(md5(uniqid(time()))' +
                    ');'#13#10#13#10#9#9'$header .= "MIME-Version: 1.0\n";'#13#10#9#9'$header .= "Content' +
                    '-Type: multipart/mixed; boundary=$boundary\n\n";'#13#10#9#9'$header .= "' +
                    'This is a multi-part message in MIME format.\n\n";'#13#10#9#9'$header .=' +
                    ' "--$boundary\n";'#13#10#9#9'$header .= "Content-Type: text/plain\n";'#13#10#9 +
                    #9'$header .= "Content-Transfer-Encoding: 8bit\n\n";'#13#10#9#9'$header .=' +
                    ' "$msg\n";'#13#10#9#9'$msg='#39#39';'#13#10#13#10#9#9#13#10#9#9'foreach ($_FILES as $filefield =' +
                    '> $file) if (is_uploaded_file($file['#39'tmp_name'#39'])) {'#13#10#9#9#9#13#10#9#9#9'$co' +
                    'ntent=chunk_split(base64_encode(fread(fopen($file['#39'tmp_name'#39'],"r' +
                    '"),filesize($file['#39'tmp_name'#39']))));'#13#10#13#10#9#9#9'$header .= "--$boundary' +
                    '\n";'#13#10#9#9#9'$header .= "Content-Type: ".$file['#39'type'#39']."; name=\"".$' +
                    'file['#39'name'#39']."\"\n";'#13#10#9#9#9'$header .= "Content-Transfer-Encoding: ' +
                    'base64\n";'#13#10#9#9#9'$header .= "Content-Disposition: attachment; file' +
                    'name=\"".$file['#39'name'#39']."\"\n\n";'#13#10#9#9#9'$header .= "$content\n";'#13#10#13 +
                    #10#9#9'}'#13#10#9#9'$header .= "--$boundary--";'#13#10#9#9'}'#13#10#9#9'mail($recipient, $su' +
                    'bject, $msg, $header);'#13#10#13#10'?>'
                  GenerateContainer = False
                end
                object Label28: TdhLabel
                  Left = 0
                  Top = 49
                  Height = 36
                  Text = 'Thank you for your message, <?php echo $_POST["name"]?>!'
                  Style.TextAlign = ctaCenter
                  Style.FontStyle = cfsItalic
                  Style.FontWeight = cfwBold
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                end
                object DirectHTML2: TdhDirectHTML
                  Left = 0
                  Top = 85
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = '<?php'#13#10#9'} else {'#13#10'?>'
                  GenerateContainer = False
                end
                object Label29: TdhLabel
                  Left = 0
                  Top = 104
                  Height = 24
                  Text = '<?php echo $err?> &nbsp;&nbsp;<Link23>Go back</Link23>'
                  Style.TextAlign = ctaCenter
                  Style.Color = Red
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                end
                object DirectHTML3: TdhDirectHTML
                  Left = 0
                  Top = 128
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = '<?php'#13#10#9'}'#13#10'?>'
                  GenerateContainer = False
                end
                object StyleSheet3: TdhStyleSheet
                  Left = 16
                  Top = 152
                  Width = 100
                  Height = 100
                  Expanded = True
                  object Link23: TdhLink
                    Left = 0
                    Top = 0
                    Height = 24
                    Text = 'Link1'
                    AutoSizeXY = asXY
                    Align = alTop
                    Right = 0
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    Link = 'javascript:history.back()'
                  end
                end
              end
            end
            object Text3: TdhLabel
              Left = 16
              Width = 143
              Height = 18
              Text = '&copy; J. Kiegeland 2010'
              Style.Effects.Enabled = True
              Style.Effects.AntiAliasing = True
              Style.Effects.Text = etInclude
              AutoSizeXY = asXY
              Anchors = [akLeft, akBottom]
              Bottom = 299
            end
          end
          object advanced: TdhPage
            Left = 8
            Top = 16
            Height = 504
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 72
            UseIFrame = False
            object Label1: TdhLabel
              Left = 16
              Top = 24
              Width = 496
              Height = 19
              Text = 'Advanced features:'
              Style.FontWeight = cfwBold
              AutoSizeXY = asY
            end
            object Label4: TdhLabel
              Left = 32
              Top = 64
              Width = 760
              Height = 338
              Text = 
                '<ul>'#10#10'<li>Checking of loops: In some situations, it may be that ' +
                'some variables surely contain no nullpointers in the first itera' +
                'tion of a loop, but in the second (or third, ...) they could be ' +
                'null (by the logic executed in a previous iteration).  Such spec' +
                'ial cases are recognized and NPE hazards reported accordingly. N' +
                'ested loops are also handled, so there are really no "leaks" in ' +
                'detecting NPE hazards.</li><br/>'#10#10'<li>Private fields: Privat cla' +
                'ss fields can be handled as local variables to some extend. So a' +
                'fter checking a private field against NULL, it can be savely acc' +
                'essed.</li><br/>'#10#10'<li>Overriding: when a method is overriden in ' +
                'a subclass, different FindNPE annotations can be declared to the' +
                ' method declaration, with the restriction, that they must be com' +
                'patible with the FindNPE annotations on the overridden method. T' +
                'his follows exactly the rationale, why  the return type of an ov' +
                'erriding method is allowed to be     a sub class of the return t' +
                'ype of the overridden method (but not the other way round).</li>' +
                '<br/>'#10#10'<li>Incremental compilation: as FindNPE is integrated int' +
                'o Eclipse'#39's Java compiler (JDT), it benefits from JDT'#39's incremen' +
                'tal compilation&nbsp;capability and automatically inherits the c' +
                'apability, that Java source files do not need to be saved in ord' +
                'er that NPE hazards can be checked. So NPE hazards are reported ' +
                '&quot;while you type&quot; and often can be solved by proposed q' +
                'uick fixes.</li>'#10#10'</ul>'
              AutoSizeXY = asY
            end
          end
        end
      end
      object StyleSheet2: TdhStyleSheet
        Left = 40
        Top = 464
        Width = 28
        Height = 28
        Expanded = False
        ExpandedWidth = 100
        ExpandedHeight = 100
        Expanded = False
        object Label8: TdhLabel
          Left = 0
          Top = 0
          Height = 18
          Text = 'Label8'
          Style.Color = -1224736768
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
        object Label11: TdhLabel
          Left = 0
          Top = 18
          Height = 18
          Text = 'Label11'
          Style.Color = Red
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
        object rule: TdhLabel
          Left = 0
          Top = 36
          Height = 18
          Text = 'rule'
          Style.TextDecoration = [ctdUnderline]
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
      end
      object Link12: TdhLink
        Left = 32
        Top = 320
        Width = 54
        Height = 18
        Text = 'Contact'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = contact
      end
      object Link13: TdhLink
        Left = 32
        Top = 200
        Width = 67
        Height = 18
        Text = 'Problems'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = features
      end
      object Link16: TdhLink
        Left = 32
        Top = 360
        Width = 70
        Height = 18
        Text = 'Advanced'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = advanced
      end
      object Link17: TdhLink
        Left = 32
        Top = 160
        Width = 102
        Height = 18
        Text = 'Getting started'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = usage
      end
    end
    object StyleSheet1: TdhStyleSheet
      Left = 1048
      Top = 552
      Width = 100
      Height = 100
      Expanded = True
      object Label3: TdhLabel
        Left = 0
        Top = 0
        Height = 18
        Text = 'Label3'
        AutoSizeXY = asY
        Align = alTop
        Right = 0
      end
      object Label5: TdhLabel
        Left = 0
        Top = 18
        Height = 18
        Text = 'Label5'
        Style.Color = Red
        AutoSizeXY = asY
        Align = alTop
        Right = 0
      end
      object Link4: TdhLink
        Left = 0
        Top = 36
        Height = 18
        Text = 'Link4'
        Style.Color = Black
        AutoSizeXY = asXY
        Align = alTop
        Right = 0
        StyleDown.Color = Blue
        StyleOver.Color = Blue
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        Link = 'http://'
      end
      object Link7: TdhLink
        Left = 0
        Top = 54
        Height = 18
        Text = 'annotations'
        AutoSizeXY = asXY
        Align = alTop
        Right = 0
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        Link = 'http://findnpe.kiegeland.com/findnpe.annotations.jar'
      end
    end
  end
end
